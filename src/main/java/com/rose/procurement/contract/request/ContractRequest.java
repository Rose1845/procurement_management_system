package com.rose.procurement.contract.request;

import com.rose.procurement.supplier.entities.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractRequest {
    private String contractTitle;
    private String contractType;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String termsAndConditions;
    private Supplier supplier;
}
