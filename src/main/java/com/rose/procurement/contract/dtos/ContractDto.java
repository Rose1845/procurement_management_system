package com.rose.procurement.contract.dtos;

import com.rose.procurement.supplier.entities.Supplier;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.rose.procurement.contract.entities.Contract}
 */
@Data
@Builder
public class ContractDto{
    private String contractTitle;
    private String contractType;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String termsAndConditions;
    private Supplier supplier;
}