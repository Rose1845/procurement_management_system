package com.rose.procurement.category.entity;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryCsvRepresentation {
    @CsvBindByName(column = "name")
    private String categoryName;
}
