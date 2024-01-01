package com.rose.procurement.contract.dtos;

import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
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
    private String contractTitle;
    private String contractType;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String termsAndConditions;
    private Set<Item> items;
    private Long vendorId;

}