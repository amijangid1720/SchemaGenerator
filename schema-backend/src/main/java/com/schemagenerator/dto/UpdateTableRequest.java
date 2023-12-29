package com.schemagenerator.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UpdateTableRequest {
    private List<Column> columns;
}
