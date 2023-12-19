package com.schemagenerator.services;

import com.schemagenerator.dto.Column;
import com.schemagenerator.dto.CreateTableRequest;
import com.schemagenerator.entity.Tables;
import com.schemagenerator.dao.TableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableRepository tableRepository;

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

    public List<String> getAllTableNames() {
        List<Tables> tables = tableRepository.findAll();
        // Assuming that your Table entity has a "tableName" property
        return tables.stream().map(Tables::getTableName).toList();
    }

}
