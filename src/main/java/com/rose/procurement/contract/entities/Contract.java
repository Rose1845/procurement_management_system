package com.rose.procurement.contract.entities;

import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long contractId;
    private String contractTitle;
    private String contractType;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String termsAndConditions;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

}
