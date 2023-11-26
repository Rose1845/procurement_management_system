package com.rose.procurement.category.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rose.procurement.category.entity.Category;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Data
public class CategoryDto{
    private String categoryName;
    public CategoryDto() {
        // No-args constructor
    }

    // Public setter
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}