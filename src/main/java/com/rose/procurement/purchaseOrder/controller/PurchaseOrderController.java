package com.rose.procurement.purchaseOrder.controller;


import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseOrder.services.PurchaseOrderService;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.services.SupplierService;
import com.rose.procurement.utils.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/purchase-order")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public PurchaseOrderDto createPurchaseOrder(@RequestBody PurchaseOrderDto purchaseOrderRequest) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderRequest);
    }

    @GetMapping
    public List<PurchaseOrder> getAllPO(){
        return purchaseOrderService.getAllOrders();

    }
    @GetMapping("/paginate")
    public ApiResponse<Page<PurchaseOrder>> findAllPurchaseOrders( @RequestParam(name = "offSet") int offSet,
                                                                  @RequestParam(name = "pageSize") int pageSize){
        Page<PurchaseOrder> purchaseOrders = purchaseOrderService.findPurchaseOrderWithPagination(offSet,pageSize);
        return new ApiResponse<>(purchaseOrders.getTotalPages(),purchaseOrders);
    }

   @GetMapping("/paginate-sorting")
    public ApiResponse<Page<PurchaseOrder>> findAllPurchaseOrderWithSorting(@RequestParam(name = "offSet") int offSet,
                                                                            @RequestParam(name = "pageSize") int pageSize,
                                                                            @RequestParam(name = "field") String field){
        Page<PurchaseOrder> purchaseOrders=
                purchaseOrderService.findAllPurchaseOrderWithPaginationAndSorting(offSet,pageSize,field);
        return new ApiResponse<>(purchaseOrders.getSize(),purchaseOrders);
    }
    @PutMapping("{id}")
    public PurchaseOrder updatePO(@PathVariable("id") Long purchaseOrderId, @RequestBody PurchaseOrderDto purchaseOrderDto){
        return purchaseOrderService.updatePurchaseOrder(purchaseOrderId,purchaseOrderDto);
    }
}
