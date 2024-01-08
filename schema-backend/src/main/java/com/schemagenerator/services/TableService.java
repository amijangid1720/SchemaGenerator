package com.schemagenerator.services;

import com.schemagenerator.dao.TableRepo.ColumnDetailsRepo;
import com.schemagenerator.dao.TableRepo.ConstraintDetailsRepo;
import com.schemagenerator.dto.*;
import com.schemagenerator.entity.ColumnDetails;
import com.schemagenerator.entity.ConstraintDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.schemagenerator.entity.Tables;
import com.schemagenerator.dao.TableRepo.TableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class TableService {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public TableService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ColumnDetailsRepo columnDetailsRepository;

    @Autowired
    private ConstraintDetailsRepo constraintDetailsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(rollbackOn = Exception.class)
    public void createTable(CreateTableRequest request) throws Exception {
        String tableName = request.getTableName();
        List<Column> columns = request.getColumns();
        System.out.println(columns);
        // Generate SQL statement for creating the table
        StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");
        for (Column column : columns) {
            sql.append(column.getName()).append(" ").append(column.getDataType());

            if (column.isPrimary()) {
                sql.append(" PRIMARY KEY");

            } else if (column.isForeignKey()) {
                sql.append(", CONSTRAINT fk_" + tableName + "_" + column.getName() +
                        " FOREIGN KEY (" + column.getName() + ") REFERENCES " +
                        column.getReferencedTable() + "(" + column.getReferencedColumn() + ") " +
                        "ON DELETE CASCADE");

            }
            sql.append(", ");

        }

        // Remove the trailing comma and space
        sql.delete(sql.length() - 2, sql.length());

        sql.append(")");

        // Add this line before jdbcTemplate.execute(sql.toString());
        System.out.println("Generated SQL Query: " + sql.toString());

        try {
            jdbcTemplate.execute(sql.toString());
            Tables tableEntity = new Tables();
            tableEntity.setTableName(tableName);
            tableRepository.save(tableEntity);
            for(Column column : columns){
                saveColumnInfo(tableName,column.getName(),column.getDataType());
                saveConstraints(tableName,column.getName(),column.isForeignKey(),column.getReferencedTable(),column.getReferencedColumn(),column.isPrimary());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error creating table", e);
        }
    }


    //to get schema of a particular table
    public TableSchemaResponse getTableSchema(String tableName) {

//        String query = "SELECT column_name, data_type, isPrimary FROM information_schema.columns WHERE table_name = :tableName";

        String query = "SELECT column_name, data_type, " +
                "CASE WHEN column_name = ANY (" +
                "   SELECT kcu.column_name " +
                "   FROM information_schema.key_column_usage kcu " +
                "   JOIN information_schema.table_constraints tc " +
                "     ON kcu.constraint_name = tc.constraint_name " +
                "   WHERE tc.table_name = :tableName AND tc.constraint_type = 'PRIMARY KEY' " +
                ") THEN true ELSE false END as isPrimary, " +
                "CASE WHEN column_name = ANY (" +
                "   SELECT kcu.column_name " +
                "   FROM information_schema.key_column_usage kcu " +
                "   JOIN information_schema.table_constraints tc " +
                "     ON kcu.constraint_name = tc.constraint_name " +
                "   WHERE tc.table_name = :tableName AND tc.constraint_type = 'FOREIGN KEY' " +
                ") THEN true ELSE false END as isForeignKey " +
                "FROM information_schema.columns " +
                "WHERE table_name = :tableName";


        List<Object[]> result = entityManager
                .createNativeQuery(query)
                .setParameter("tableName", tableName)
                .getResultList();


        System.out.println("Generated SQL Query: " + query);

        //convert result to List<Column>

        List<Column> columns = result.stream()
                .map(row -> new Column((String) row[0], null, (boolean) row[2], false, (String) row[1], null,(Boolean) row[3],null,null))
                .toList();


        return new TableSchemaResponse(columns);


    }


    public List<String> getAllTableNames() {
        List<Tables> tables = tableRepository.findAll();
        // Assuming that your Table entity has a "tableName" property
        return tables.stream().map(Tables::getTableName).toList();
    }

    public ResponseEntity<ApiResponse> deleteTable(String tableName) {
        Tables table = tableRepository.findByTableName(tableName).get();
        // Formulate the SQL query to drop the table
        String sql = "DROP TABLE " + tableName;

        // Execute the query
        jdbcTemplate.execute(sql);
        tableRepository.delete(table);
        // If the execution reaches here, the table was deleted successfully
        return ResponseEntity.status(200).body(new ApiResponse("Table deleted successfully"));
    }

    // In TableService.java

    public List<Map<String, Object>> getTableData(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching table data", e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateTableSchema(String tableName, List<Column> updatedColumns) throws Exception {
        boolean flag = false;
        try {
            for (Column updatedColumn : updatedColumns) {
                // If the column name is updated, use the CHANGE COLUMN clause
                if (!updatedColumn.getName().equals(updatedColumn.getOldName())) {
                    alterColumnName(tableName, updatedColumn.getOldName(), updatedColumn.getName());
                }

                // If the data type is updated, use the MODIFY COLUMN clause
                if (!updatedColumn.getDataType().equals(updatedColumn.getOlddataType())) {
                    alterColumnType(tableName, updatedColumn.getName(), updatedColumn.getDataType());
                }

                // If the primary key is updated, handle it accordingly
                if (updatedColumn.isPrimary() != updatedColumn.isOldIsPrimary()) {
                    //Drop the tables primary key
                    flag = true;

                }
            }

            if (flag) {
                dropTablesPrimaryKey(tableName);
                for (Column column : updatedColumns) {
                    alterPrimaryKey(tableName, column.getName(), column.isPrimary());
                }
            }
        } catch (Exception e) {
            throw new Exception("Error updating table schema" + e.getMessage());
        }
    }

    private void alterColumnName(String tableName, String oldName, String newName) {
        String sql = "ALTER TABLE " + tableName + " RENAME COLUMN " + oldName + " " + "TO" + " " + newName;
        jdbcTemplate.execute(sql);

    }

    private void alterColumnType(String tableName, String columnName, String dataType) {
        dataType = dataType.toUpperCase();
        if (dataType.equals("VARCHAR(255)"))
            dataType = "VARCHAR";
        String sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + columnName + " " + "TYPE" + " " + dataType + " " + "USING " + columnName + "::" + dataType;
//


        try {
            jdbcTemplate.execute(sql);
        } catch (Exception s) {
            System.out.println(s.getMessage());
        }
    }


    @Transactional

    private void alterPrimaryKey(String tableName, String columnName, boolean newPrimary) {
        try {

            if (newPrimary) {
                String addPrimaryKeySql = "ALTER TABLE " + tableName + " ADD PRIMARY KEY (" + columnName + ")";
                jdbcTemplate.execute(addPrimaryKeySql);
            }

            System.out.println("Operation successful");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void dropTablesPrimaryKey(String tableName) {
        String constraintQuery = "SELECT constraint_name " +
                "FROM information_schema.table_constraints " +
                "WHERE table_name = '" + tableName.toLowerCase() + "' AND constraint_type = 'PRIMARY KEY'";

        String constraintName = jdbcTemplate.queryForObject(constraintQuery, String.class);

        // Drop the primary key constraint
        String dropConstraintSql = "ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraintName;
        jdbcTemplate.execute(dropConstraintSql);
    }

    @Transactional(rollbackOn = Exception.class)
    public void addRowData(String tableName, Map<String, Object> data) throws Exception {
        // Formulate the SQL query to insert data into the table
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder(") VALUES (");

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sql.append(entry.getKey()).append(", ");
            values.append(":" + entry.getKey() + ", ");
        }

        // Remove the trailing commas and spaces
        sql.delete(sql.length() - 2, sql.length());
        values.delete(values.length() - 2, values.length());

        sql.append(values).append(")");

        // Print the generated SQL query
        System.out.println("Generated SQL Query: " + sql.toString());

        try {
            // Convert data values to appropriate types before executing the query
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object convertedValue = convertToDataType(entry.getValue());
                parameterSource.addValue(entry.getKey(), convertedValue);

                // Print the data type information
//                System.out.println("Data Type of " + entry.getKey() + ": " + convertedValue.getClass().getSimpleName());
            }

            namedParameterJdbcTemplate.update(sql.toString(), parameterSource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error adding data to table", e);
        }
    }


    private Object convertToDataType(Object value) {
        if (value instanceof String) {
            return value;
        } else if (value instanceof Integer) {
            return value;
        } else if (value instanceof Long) {
            return value;
        } else if (value instanceof Double) {
            return value;
        } else if (value instanceof Float) {
            return value;
        } else if (value instanceof Boolean) {
            return value;
        } else {
            // Handle other data types as needed
            return value.toString(); // Default to String if the type is not recognized
        }
    }

    public void addForeignKey(ForeignKeyRequest request) throws Exception {
        String tableName = request.getTableName();
        String columnName = request.getColumnName();
        String referencedTable = request.getReferencedTable();
        String referencedColumn = request.getReferencedColumn();

        try {
            // Generate SQL statement for adding a foreign key constraint
            String sql = "ALTER TABLE " + tableName +
                    " ADD CONSTRAINT fk_" + tableName + "_" + columnName +
                    " FOREIGN KEY (" + columnName + ") REFERENCES " +
                    referencedTable + "(" + referencedColumn + ") ON DELETE CASCADE";

            // Add this line before jdbcTemplate.execute(sql);
            System.out.println("Generated SQL Query: " + sql);

            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error adding foreign key constraint", e);
        }
    }
    public void saveColumnInfo(String tableName, String columnName, String dataType) {
        ColumnDetails columnInfo = new ColumnDetails();
        columnInfo.setTableName(tableName);
        columnInfo.setColumnName(columnName);
        columnInfo.setDataType(dataType);

        columnDetailsRepository.save(columnInfo);
    }

    //saveConstraints(tableName,column.getName(),column.isForeignKey(),column.getReferencedTable(),column.getReferencedColumn(),column.isPrimary());

    public void saveConstraints(String tableName, String columnName,Boolean foreignKey,String referencedTable,String referencedColumn,Boolean primary)
    {
        ConstraintDetails constraintDetails = new ConstraintDetails();
        constraintDetails.setTableName(tableName);
        constraintDetails.setColumnName(columnName);
        constraintDetails.setForeignKey(foreignKey);
        constraintDetails.setReferencedTable(referencedTable);
        constraintDetails.setReferencedColumn(referencedColumn);
        constraintDetails.setPrimaryKey(primary);
        constraintDetailsRepository.save(constraintDetails);
    }
}
