package com.rose.procurement.category.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rose.procurement.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Data
public class CategoryDto{
    @NotNull
    @NotEmpty
    @NotBlank
    private String categoryName;

    public CategoryDto() {
        // Default constructor
    }
}