package com.rose.procurement.purchaseOrder.mappers;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderMapperImpl implements PurchaseOrderMapper{
    private final SupplierRepository supplierRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public PurchaseOrder toEntity(PurchaseOrderDto purchaseOrderDto) {

        Optional<Supplier> supplier = supplierRepository.findByVendorId(purchaseOrderDto.getSupplier().getVendorId());
        // Separating existing and new suppliers
        if(supplier.isEmpty()){
            throw new IllegalStateException();
        }
        Optional<Category> category = categoryRepository.findByCategoryId(purchaseOrderDto.getCategory().getCategoryId());
        // Separating existing and new suppliers
        if(category.isEmpty()){
            throw new IllegalStateException();
        }


        // Creating the purchase order with the selected suppliers and items
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .purchaseOrderTitle(purchaseOrderDto.getPurchaseOrderTitle())
                .deliveryDate(purchaseOrderDto.getDeliveryDate())
                .termsAndConditions(purchaseOrderDto.getTermsAndConditions())
                .paymentType(purchaseOrderDto.getPaymentType())
                .supplier(supplier.get())
                .category(category.get())
                .build();
        BeanUtils.copyProperties(purchaseOrder,purchaseOrderDto);
        return purchaseOrder;
    }

    @Override
    public PurchaseOrderDto toDto(PurchaseOrder purchaseOrder) {
        Optional<Supplier> supplier = supplierRepository.findByVendorId(purchaseOrder.getSupplier().getVendorId());
        // Separating existing and new suppliers
        if(supplier.isEmpty()){
            throw new IllegalStateException();
        }
        Optional<Category> category = categoryRepository.findByCategoryId(purchaseOrder.getCategory().getCategoryId());
        // Separating existing and new suppliers
        if(category.isEmpty()){
            throw new IllegalStateException();
        }


        // Creating the purchase order with the selected suppliers and items
        PurchaseOrderDto purchaseOrderDto = PurchaseOrderDto.builder()
                .purchaseOrderTitle(purchaseOrder.getPurchaseOrderTitle())
                .deliveryDate(purchaseOrder.getDeliveryDate())
                .termsAndConditions(purchaseOrder.getTermsAndConditions())
                .paymentType(purchaseOrder.getPaymentType())
                .supplier(supplier.get())
                .category(category.get())
                .build();
        BeanUtils.copyProperties(purchaseOrderDto,purchaseOrder);

        return purchaseOrderDto;
    }

    @Override
    public PurchaseOrder partialUpdate(PurchaseOrderDto purchaseOrderDto, PurchaseOrder purchaseOrder) {
        return null;
    }
}
