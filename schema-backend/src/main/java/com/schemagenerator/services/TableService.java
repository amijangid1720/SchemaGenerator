package com.schemagenerator.services;

import com.schemagenerator.dto.Column;
import com.schemagenerator.dto.CreateTableRequest;
import com.schemagenerator.dto.TableSchemaResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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


}
