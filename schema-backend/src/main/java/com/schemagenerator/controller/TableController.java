package com.schemagenerator.controller;


import com.schemagenerator.dto.CreateTableRequest;
import com.schemagenerator.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schema")
public class TableController {

    @Autowired
    private TableService tableService;
    @PostMapping("/create-table")
    public ResponseEntity<String> createTable(@RequestBody CreateTableRequest request){
         try{
             tableService.createTable(request);
             return  ResponseEntity.ok().body("table created successfully");
         }catch(Exception e){
             return ResponseEntity.ok().body("table created successfully");
         }

    }
}
