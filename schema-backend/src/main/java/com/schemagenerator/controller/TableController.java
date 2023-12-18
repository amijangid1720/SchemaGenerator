package com.schemagenerator.controller;


import com.schemagenerator.dto.ApiResponse;
import com.schemagenerator.dto.CreateTableRequest;
import com.schemagenerator.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/schema")
public class TableController {

    @Autowired
    private TableService tableService;
    @PostMapping("/create-table")
    public ResponseEntity<ApiResponse> createTable(@RequestBody CreateTableRequest request){
         try{
             System.out.println(request);
             tableService.createTable(request);
             return  ResponseEntity.ok().body(new ApiResponse("Table created sucecssfully"));
         }catch(Exception e){
//             return ResponseEntity.ok().body("table not created");
             return ResponseEntity.status(500).body(new ApiResponse("Failed to create table"));
         }

    }
}
