package com.schemagenerator.dao;

import com.schemagenerator.entity.ColumnDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColumnDetailsRepo extends JpaRepository<ColumnDetails,Long> {

    Optional<ColumnDetails> findByTableNameAndColumnName(String tableName, String oldColumnName);

    List<ColumnDetails> findByTableName(String tableName);
}
