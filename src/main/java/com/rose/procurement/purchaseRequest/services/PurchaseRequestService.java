package com.rose.procurement.purchaseRequest.services;

import com.rose.procurement.email.service.EmailService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import com.rose.procurement.purchaseRequest.mappers.PurchaseRequestMapper;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierOffer;
import com.rose.procurement.supplier.services.SupplierOfferService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestMapper purchaseRequestMapper ;
    private final SupplierOfferService supplierOfferService;
    private final EmailService emailService;

    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository,
                                  PurchaseRequestMapper purchaseRequestMapper, SupplierOfferService supplierOfferService, EmailService emailService) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestMapper = purchaseRequestMapper;
        this.supplierOfferService = supplierOfferService;
        this.emailService = emailService;
    }

    public PurchaseRequestDto createPurchaseRequest(PurchaseRequestDto purchaseRequest) {

        PurchaseRequest purchaseRequest1 = PurchaseRequestMapper.INSTANCE.toEntity(purchaseRequest);
        purchaseRequest1.setPurchaseRequestTitle(purchaseRequest1.getPurchaseRequestTitle());
        purchaseRequest1.setDueDate(purchaseRequest.getDueDate());
        purchaseRequest1.setApprovalStatus(ApprovalStatus.PENDING);
        Set<Item> items = new HashSet<>(purchaseRequest1.getItems());
        purchaseRequest1.setItems(new HashSet<>(items));
        Set<Supplier> suppliers = new HashSet<>(purchaseRequest1.getSuppliers());
        purchaseRequest1.setSuppliers(new HashSet<>(suppliers));
        PurchaseRequest savedPrequest = purchaseRequestRepository.save(purchaseRequest1);
        PurchaseRequestDto savedDTo = PurchaseRequestMapper.INSTANCE.toDto(savedPrequest);
        // Additional logic or validation can be added here before saving
        return savedDTo;
    }

    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll();
    }

    // Get a purchase request by ID
    public Optional<PurchaseRequestDto> getPurchaseRequestById(Long purchaseRequestId) {
        Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);
        return purchaseRequest.map(purchaseRequestMapper::toDto);
    }

    // Associate a Purchase Order with a Purchase Request
    public PurchaseRequest createPurchaseOrder(Long purchaseRequestId, PurchaseOrder purchaseOrder) {
        Optional<PurchaseRequest> optionalPurchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);

        if (optionalPurchaseRequest.isPresent()) {
            PurchaseRequest purchaseRequest = optionalPurchaseRequest.get();
//            purchaseRequest.setSuppliers(purchaseOrder.ge);
            return purchaseRequestRepository.save(purchaseRequest);
        } else {
            // Handle the case where the purchase request with the given ID is not found
            throw new EntityNotFoundException("Purchase Request not found with ID: " + purchaseRequestId);
        }
    }

    public void submitPurchaseRequestToSuppliers(Long purchaseRequestId) {
        // Retrieve the purchase request by ID
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        // Validate that the purchase request is in a valid state for submission
        for (Supplier supplier : purchaseRequest.getSuppliers()) {
            // For each item in the purchase request, create a placeholder supplier offer
            for (Item purchaseRequestItem : purchaseRequest.getItems()) {
                SupplierOffer supplierOffer = new SupplierOffer();
                supplierOffer.setItems((Set<Item>) purchaseRequestItem);
                // Set default values or leave them null for suppliers to fill in
                // This could include unit price, delivery time, etc.
//                supplierOffer.setUnitPrice();
                supplierOfferService.addSupplierOffer(purchaseRequestId,supplierOffer);
                emailService.sendEmail(supplier.getEmail(),"Purchase Request Offer","PLease check the offer");
            }
        }


        // Update the purchase request status or perform any other necessary actions

        // You might want to notify suppliers via email, messaging, etc.
    }


}
