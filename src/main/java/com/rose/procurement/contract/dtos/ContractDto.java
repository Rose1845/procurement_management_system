package com.rose.procurement.contract.dtos;

import com.rose.procurement.items.entity.Item;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link com.rose.procurement.contract.entities.Contract}
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto{;
    @NotNull
    @NotBlank
    private String contractTitle;
    @NotNull
    @NotBlank
    private String contractType;
    @NotNull
    @PastOrPresent
    private LocalDate contractStartDate;
    @NotNull
    @FutureOrPresent
    private LocalDate contractEndDate;
    @NotNull
    @NotBlank
    private String termsAndConditions;
    @NotNull
    private Set<Item> items;
    @NotNull
    private String vendorId;

}