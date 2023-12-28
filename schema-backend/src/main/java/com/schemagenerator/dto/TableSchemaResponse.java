package com.schemagenerator.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TableSchemaResponse {
     List<Column> columns;
}
