package com.schemagenerator.controller;


import com.schemagenerator.dto.ApiResponse;
import com.schemagenerator.dto.Column;
import com.schemagenerator.dto.CreateTableRequest;
import com.schemagenerator.dto.TableSchemaResponse;
import com.schemagenerator.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;

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
    public ResponseEntity<String> deleteTable(@PathVariable String tableName) {
       try {

           return tableService.deleteTable(tableName);
       }catch (Exception e)
       {
           return ResponseEntity.status(500).body("Error deleting table: " + e.getMessage());
       }

    }
}
