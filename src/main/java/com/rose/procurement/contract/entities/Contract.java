package com.rose.procurement.contract.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String contractId;
    private String contractTitle;
    private String contractType;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String termsAndConditions;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    private Supplier supplier;

}
