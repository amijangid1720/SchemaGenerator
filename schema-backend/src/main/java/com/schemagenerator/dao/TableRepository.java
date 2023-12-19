package com.schemagenerator.dao;

import com.schemagenerator.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Tables, Long> {

    // Add custom query methods if needed
}