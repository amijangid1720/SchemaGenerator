package com.schemagenerator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@jakarta.persistence.Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ColumnDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableName;
    private String columnName;
    private String dataType;
}
