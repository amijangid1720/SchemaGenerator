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
    private String oldName;
    private boolean isPrimary;
    private boolean oldIsPrimary;
    private String dataType;
    private String olddataType;
}
