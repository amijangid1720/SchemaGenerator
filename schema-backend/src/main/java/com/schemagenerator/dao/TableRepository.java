package com.schemagenerator.dao;

import com.schemagenerator.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableRepository extends JpaRepository<Tables, Long> {

    Optional<Tables> findByTableName(String tableName);
}