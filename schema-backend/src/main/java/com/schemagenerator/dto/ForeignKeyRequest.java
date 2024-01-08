package com.schemagenerator.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class ForeignKeyRequest {
    private String tableName;
    private String columnName;
    private String referencedTable;
    private String referencedColumn;
}
