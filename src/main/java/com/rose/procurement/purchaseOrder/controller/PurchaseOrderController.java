package com.rose.procurement.purchaseOrder.controller;


import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.request.PurchaseOrderRequest;
import com.rose.procurement.purchaseOrder.services.PurchaseOrderService;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.services.SupplierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/purchase-order")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final SupplierService supplierService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, SupplierService supplierService) {
        this.purchaseOrderService = purchaseOrderService;
        this.supplierService = supplierService;
    }

    @PostMapping
    public PurchaseOrder createPurchaseOrder(@RequestBody PurchaseOrderRequest purchaseOrderRequest) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderRequest);
    }
    @GetMapping("/{purchaseOrderId}/suppliers")
    public Optional<Supplier> getSuppliersForPurchaseOrder(@PathVariable Long purchaseOrderId) {
        return purchaseOrderService.getSuppliersForPurchaseOrder(purchaseOrderId);
    }







        }
