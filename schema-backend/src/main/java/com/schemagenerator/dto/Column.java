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
    private boolean isForeignKey;
    private String referencedTable;
    private String referencedColumn;

    public Column(String name, boolean isPrimary, String dataType, boolean isForeignKey) {
        this.name = name;
        this.isPrimary = isPrimary;
        this.dataType = dataType;
        this.isForeignKey = isForeignKey;
    }
}
