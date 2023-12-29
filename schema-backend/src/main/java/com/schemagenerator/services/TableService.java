package com.schemagenerator.services;

import com.schemagenerator.dto.ApiResponse;
import com.schemagenerator.dto.Column;
import com.schemagenerator.dto.CreateTableRequest;
import com.schemagenerator.dto.TableSchemaResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.schemagenerator.entity.Tables;
import com.schemagenerator.dao.TableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class TableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableRepository tableRepository;


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
                ") THEN true ELSE false END as isPrimary " +
                "FROM information_schema.columns " +
                "WHERE table_name = :tableName";


        List<Object[]> result = entityManager
                .createNativeQuery(query)
                .setParameter("tableName", tableName)
                .getResultList();


        System.out.println("Generated SQL Query: " + query);

        //convert result to List<Column>

        List<Column> columns = result.stream()
                .map(row -> new Column((String) row[0], null, (boolean) row[2], false, (String) row[1], null))
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
        boolean flag=false;
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
                    flag=true;

                }
            }

            if(flag)
            {
                dropTablesPrimaryKey(tableName);
                for(Column column : updatedColumns)
                {
                    alterPrimaryKey(tableName,column.getName(),column.isPrimary());
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

        System.out.println("3");
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

    public void dropTablesPrimaryKey( String tableName){
        String constraintQuery = "SELECT constraint_name " +
                "FROM information_schema.table_constraints " +
                "WHERE table_name = '" + tableName.toLowerCase() + "' AND constraint_type = 'PRIMARY KEY'";

        String constraintName = jdbcTemplate.queryForObject(constraintQuery, String.class);

        // Drop the primary key constraint
        String dropConstraintSql = "ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraintName;
        jdbcTemplate.execute(dropConstraintSql);
    }

}
