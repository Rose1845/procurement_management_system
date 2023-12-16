package com.rose.procurement.utils;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.stream.Stream;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    int recordCount;
    T response;

    public ApiResponse(Stream<PurchaseOrder> purchaseOrderStream, Page<PurchaseOrder> purchaseOrders) {
    }
}
