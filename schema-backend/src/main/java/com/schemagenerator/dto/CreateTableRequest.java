package com.schemagenerator.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateTableRequest {
    private String tableName;
    private List<Column> columns;
}
