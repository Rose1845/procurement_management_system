package com.rose.procurement.purchaseRequest.services;

import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import com.rose.procurement.purchaseRequest.mappers.PurchaseRequestMapper;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final ItemRepository itemRepository;

    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository,
                                  ItemRepository itemRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.itemRepository = itemRepository;
    }

    public PurchaseRequestDto createPurchaseRequest(PurchaseRequestDto purchaseRequest) {
        PurchaseRequest purchaseRequest1 = PurchaseRequestMapper.INSTANCE.toEntity(purchaseRequest);
        purchaseRequest1.setPurchaseRequestTitle(purchaseRequest1.getPurchaseRequestTitle());
        purchaseRequest1.setDueDate(purchaseRequest.getDueDate());
        Set<Item> items = new HashSet<>(purchaseRequest1.getItems());
        purchaseRequest1.setItems(new HashSet<>(items));        PurchaseRequest savedPrequest = purchaseRequestRepository.save(purchaseRequest1);
        PurchaseRequestDto savedDTo = PurchaseRequestMapper.INSTANCE.toDto(savedPrequest);
        // Additional logic or validation can be added here before saving
        return savedDTo;
    }

    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll();
    }

    // Get a purchase request by ID
    public Optional<PurchaseRequest> getPurchaseRequestById(Long purchaseRequestId) {
        return purchaseRequestRepository.findById(purchaseRequestId);
    }

    // Associate a Purchase Order with a Purchase Request
    public PurchaseRequest createPurchaseOrder(Long purchaseRequestId, PurchaseOrder purchaseOrder) {
        Optional<PurchaseRequest> optionalPurchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);

        if (optionalPurchaseRequest.isPresent()) {
            PurchaseRequest purchaseRequest = optionalPurchaseRequest.get();
            purchaseRequest.setSupplier(purchaseOrder.getSupplier());
            return purchaseRequestRepository.save(purchaseRequest);
        } else {
            // Handle the case where the purchase request with the given ID is not found
            throw new EntityNotFoundException("Purchase Request not found with ID: " + purchaseRequestId);
        }
    }

}
