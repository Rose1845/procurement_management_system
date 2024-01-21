package com.rose.procurement.purchaseOrder.controller;


import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseOrder.services.PurchaseOrderService;
import com.rose.procurement.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/v1/purchase-order")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public PurchaseOrderDto createPurchaseOrder(@RequestBody @Valid PurchaseOrderDto purchaseOrderRequest) {
        return purchaseOrderService.createPurchaseOrder(purchaseOrderRequest);
    }
    @GetMapping("{id}")
    public Optional<PurchaseOrder> getPurchaseOrderById(@PathVariable("id") Long purchaseOrderId){
        return purchaseOrderService.findPurchaseOrderById(purchaseOrderId);
    }

    @GetMapping
    public List<PurchaseOrder> getAllPO(){
        return purchaseOrderService.getAllOrders();
    }
    @GetMapping("/order-items/{id}")
    public Optional<PurchaseOrderDto> getPurchaseOrderWithItems(@PathVariable("id") Long purchaseOrderId) {
        return purchaseOrderService.getPurchaseOrderWithItems(purchaseOrderId);
    }
    @GetMapping("/paginate")
    public ApiResponse<Page<PurchaseOrder>> findAllPurchaseOrders( @RequestParam(name = "offSet") int offSet,
                                                                  @RequestParam(name = "pageSize") int pageSize){
        Page<PurchaseOrder> purchaseOrders = purchaseOrderService.findPurchaseOrderWithPagination(offSet,pageSize);
        return new ApiResponse<>(purchaseOrders.getTotalPages(),purchaseOrders);
    }
    @GetMapping("/{purchaseOrderId}/items")
    public ResponseEntity<Set<Item>> getItemsForPurchaseOrder(@PathVariable Long purchaseOrderId) {
        Set<Item> items = purchaseOrderService.getItemsForPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(items);
    }
    @GetMapping("purchase-details/{purchaseOrderId}")
    public List<Object[]> getPurchaseDetailsByPurchaseOrderId(@PathVariable("purchaseOrderId") Long purchaseOrderId){
        return purchaseOrderService.findPurchaseOrderDetailsByPurchaseOrderId(purchaseOrderId);
    }
   @GetMapping("/paginate-sorting")
    public ApiResponse<Page<PurchaseOrder>> findAllPurchaseOrderWithSorting(@RequestParam(name = "offSet") int offSet,
                                                                            @RequestParam(name = "pageSize") int pageSize,
                                                                            @RequestParam(name = "field") String field){
        Page<PurchaseOrder> purchaseOrders=
                purchaseOrderService.findAllPurchaseOrderWithPaginationAndSorting(offSet,pageSize,field);
        return new ApiResponse<>(purchaseOrders.getSize(),purchaseOrders);
    }
    @GetMapping("{purchaseOrderTitle}")
    public PurchaseOrder getPurchaseOrderByPurchaseOrderTitle(@PathVariable("purchaseOrderTitle") String purchaseOrderTitle){
        return purchaseOrderService.getPurchaseOrderByPurchaseOrderTitle(purchaseOrderTitle);

    }
    @GetMapping("p-orders/{month}")
    public List<PurchaseOrder> findPurchaseOrdersByMonth(@PathVariable("month") int month){
        return  purchaseOrderService.findPurchaseOrdersByMonth(month);
    }
    @PutMapping("{id}")
    public PurchaseOrder updatePO(@PathVariable("id") Long purchaseOrderId, @RequestBody PurchaseOrderDto purchaseOrderDto){
        return purchaseOrderService.updatePurchaseOrder(purchaseOrderId,purchaseOrderDto);
    }
}
