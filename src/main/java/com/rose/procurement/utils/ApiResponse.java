package com.rose.procurement.utils;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private long recordCount; // Consider changing to long to handle large datasets
    private T response;
    private int totalPages;
    private long totalElements;

    // Constructor for pagination
    public ApiResponse(Page<PurchaseOrder> purchaseOrders) {
        this.recordCount = purchaseOrders.getSize();
        this.response = (T) purchaseOrders.getContent(); // Get the actual list of PurchaseOrders
        this.totalPages = purchaseOrders.getTotalPages();
        this.totalElements = purchaseOrders.getTotalElements();
    }


}
