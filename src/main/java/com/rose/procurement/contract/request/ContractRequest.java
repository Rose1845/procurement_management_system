package com.rose.procurement.contract.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractRequest {
    private Long vendorId;
    private String contractTitle;
    private String contractType;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String termsAndConditions;
}
