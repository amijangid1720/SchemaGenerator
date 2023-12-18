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
    private boolean isPrimary;
    private String dataType;
}
