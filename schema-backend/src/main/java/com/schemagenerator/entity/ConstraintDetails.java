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
public class ConstraintDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tableName;
    private String columnName;
    private boolean foreignKey;
    private String referencedTable;
    private String referencedColumn;
    private boolean primaryKey;

    public ConstraintDetails(String tableName, String columnName, boolean foreignKey, boolean primaryKey) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.foreignKey = foreignKey;
        this.primaryKey = primaryKey;
    }
}
