package com.rose.procurement.contract.dtos;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RenewDto {
    @NotNull
    @FutureOrPresent
    private LocalDate contractEndDate;
}
