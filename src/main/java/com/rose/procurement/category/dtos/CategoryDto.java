package com.rose.procurement.category.dtos;

import com.rose.procurement.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for {@link Category}
 */
@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
public class CategoryDto{
    @NotNull
    @NotEmpty
    @NotBlank
    private String categoryName;

}