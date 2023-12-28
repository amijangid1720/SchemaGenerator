package com.schemagenerator.services;

import com.schemagenerator.dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import com.schemagenerator.entity.Tables;
import com.schemagenerator.dao.TableRepo.TableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
                .map(row -> new Column((String) row[0], (boolean) row[2], (String) row[1]))
                .toList();


        return new TableSchemaResponse(columns);


    }


    public List<String> getAllTableNames() {
        List<Tables> tables = tableRepository.findAll();
        // Assuming that your Table entity has a "tableName" property
        return tables.stream().map(Tables::getTableName).toList();
    }

    public ResponseEntity<ApiResponse> deleteTable(String tableName) {
        Tables table=tableRepository.findByTableName(tableName).get();
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
}
