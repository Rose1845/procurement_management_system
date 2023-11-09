package com.rose.procurement.purchaseOrder.services;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.service.ItemService;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.purchaseOrder.request.PurchaseOrderRequest;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import com.rose.procurement.supplier.request.SupplierRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, SupplierRepository supplierRepository, CategoryRepository categoryRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.supplierRepository = supplierRepository;
        this.categoryRepository = categoryRepository;
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrderRequest purchaseOrderRequest) {
        Optional<Supplier> supplier = supplierRepository.findByVendorId(purchaseOrderRequest.getSupplier().getVendorId());
        // Separating existing and new suppliers
      if(supplier.isEmpty()){
          throw new IllegalStateException();
      }
        Optional<Category> category = categoryRepository.findByCategoryId(purchaseOrderRequest.getCategory().getCategoryId());
        // Separating existing and new suppliers
        if(category.isEmpty()){
            throw new IllegalStateException();
        }

        // Creating the purchase order with the selected suppliers and items
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .purchaseOrderTitle(purchaseOrderRequest.getPurchaseOrderTitle())
                .deliveryDate(purchaseOrderRequest.getDeliveryDate())
                .termsAndConditions(purchaseOrderRequest.getTermsAndConditions())
                .paymentTerms(purchaseOrderRequest.getPaymentTerms())
                .supplier(supplier.get())
                .category(category.get())
                .build();

        // Additional validation or business logic can be added here
        return purchaseOrderRepository.save(purchaseOrder);
    }
    public Optional<Supplier> getSuppliersForPurchaseOrder(Long purchaseOrderId) {
        return purchaseOrderRepository.findSuppliersByPurchaseOrderId(purchaseOrderId);
    }
    public void addSupplierToPurchaseOrder(Long purchaseOrderId, SupplierRequest supplierRequest) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));

        Supplier supplier = Supplier.builder()
                .name(supplierRequest.getName())
                .address(supplierRequest.getAddress())
                .contactInformation(supplierRequest.getContactInformation())
                .contactPerson(supplierRequest.getContactPerson())
                .email(supplierRequest.getEmail())
                .paymentTerms(supplierRequest.getPaymentTerms())
                .phoneNumber(supplierRequest.getPhoneNumber())
                .termsAndConditions(supplierRequest.getTermsAndConditions())
                .build();
        purchaseOrder.getSupplier();
        purchaseOrderRepository.save(purchaseOrder);
    }

}