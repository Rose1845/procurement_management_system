package com.rose.procurement.purchaseOrder.services;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.contract.repository.ContractRepository;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrderDto;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseOrderService {

private final PurchaseOrderRepository purchaseOrderRepository;
private final ContractRepository contractRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, ContractRepository contractRepository,
                                PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.contractRepository = contractRepository;
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    public PurchaseOrderDto createPurchaseOrder(PurchaseOrderDto purchaseOrderRequest) {
        Optional<Contract> contract = contractRepository.findById(purchaseOrderRequest.getContract().getContractId());
       if(contract.isEmpty()){
           throw new IllegalStateException();
        }


        // Creating the purchase order with the selected suppliers and items
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .purchaseOrderTitle(purchaseOrderRequest.getPurchaseOrderTitle())
                .deliveryDate(purchaseOrderRequest.getDeliveryDate())
                .termsAndConditions(purchaseOrderRequest.getTermsAndConditions())
                .paymentType(purchaseOrderRequest.getPaymentType())
                .contract(contract.get())
                .build();
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toDto(savedPurchaseOrder);
    }
    public Optional<Supplier> getSuppliersForPurchaseOrder(Long purchaseOrderId) {
        return purchaseOrderRepository.findSuppliersByPurchaseOrderId(purchaseOrderId);
    }


}
