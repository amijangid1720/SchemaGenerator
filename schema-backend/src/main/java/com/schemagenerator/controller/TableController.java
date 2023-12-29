package com.schemagenerator.controller;


import com.schemagenerator.dto.*;
import com.schemagenerator.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/schema")
public class TableController {

    @Autowired
    private TableService tableService;

    @PostMapping("/create-table")
    public ResponseEntity<ApiResponse> createTable(@RequestBody CreateTableRequest request) {
        try {
            System.out.println(request);
            tableService.createTable(request);
            return ResponseEntity.ok().body(new ApiResponse("Table created sucecssfully"));
        } catch (Exception e) {
//             return ResponseEntity.ok().body("table not created");
            return ResponseEntity.status(500).body(new ApiResponse("Failed to create table"));
        }
    }


    @GetMapping("/getSchema/{tableName}")
    public TableSchemaResponse getTableSchema(@PathVariable String tableName) {
        return tableService.getTableSchema(tableName);
    }


    @GetMapping("/tables")
    public ResponseEntity<List<String>> getAllTableNames() {
        List<String> tableNames = tableService.getAllTableNames();
        return ResponseEntity.ok().body(tableNames);
    }

    @DeleteMapping("/deleteTable/{tableName}")
    public ResponseEntity<ApiResponse> deleteTable(@PathVariable String tableName) {
       try {

           return tableService.deleteTable(tableName);
       }catch (Exception e)
       {
           return ResponseEntity.status(500).body(new ApiResponse("Error deleting table: " + e.getMessage()));
       }

    }


    // In TableController.java

    @GetMapping("/getData/{tableName}")
    public ResponseEntity<List<Map<String, Object>>> getTableData(@PathVariable String tableName) {
        try {
            List<Map<String, Object>> tableData = tableService.getTableData(tableName);
            return ResponseEntity.ok().body(tableData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }

    @PutMapping("/update-schema/{tableName}")
    public ResponseEntity<ApiResponse> updateTableSchema(@PathVariable String tableName, @RequestBody UpdateTableRequest updatedSchema) {
        try {
            List<Column>updatedColumns=updatedSchema.getColumns();
            tableService.updateTableSchema(tableName, updatedColumns);
            return ResponseEntity.ok().body(new ApiResponse("Table schema updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Failed to update table schema"));
        }
    }

}
