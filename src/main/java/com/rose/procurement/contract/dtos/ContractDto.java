package com.rose.procurement.contract.dtos;

import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.websocket.server.ServerEndpoint;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.rose.procurement.contract.entities.Contract}
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto{
    @NotNull
    @NotBlank
    private String contractTitle;
    @NotNull
    @NotBlank
    private String contractType;
    @NotNull
    @NotBlank
    @PastOrPresent
    private LocalDate contractStartDate;
    @NotNull
    @NotBlank
    @FutureOrPresent
    private LocalDate contractEndDate;
    @NotNull
    @NotBlank
    private String termsAndConditions;
    @NotNull
    @NotBlank
    private Set<Item> items;
    @NotNull
    @NotBlank
    private Long vendorId;

}