package com.schemagenerator.dto;
import lombok.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableDataResponse {
    private List<Map<String, Object>> data;
}
