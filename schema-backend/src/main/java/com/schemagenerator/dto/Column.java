package com.schemagenerator.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Column {
    private String name;
    private String datatype;
    private boolean primaryKey;
}
