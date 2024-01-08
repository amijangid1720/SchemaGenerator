package com.schemagenerator.dao;

import com.schemagenerator.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TableRepository extends JpaRepository<Tables, Long> {

    Optional<Tables> findByTableName(String tableName);


    // Custom query method to find a table by name and modify a column


}