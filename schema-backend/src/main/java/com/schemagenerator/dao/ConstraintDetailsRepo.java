package com.schemagenerator.dao;

import com.schemagenerator.entity.ColumnDetails;
import com.schemagenerator.entity.ConstraintDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConstraintDetailsRepo extends JpaRepository<ConstraintDetails,Integer> {


    List<ConstraintDetails> findByTableName(String tableName);

    Optional<ConstraintDetails> findByTableNameAndPrimaryKey(String tableName, boolean b);
}
