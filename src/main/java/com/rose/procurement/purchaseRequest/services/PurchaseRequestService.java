package com.rose.procurement.purchaseRequest.services;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.email.service.EmailService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.offer.*;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import com.rose.procurement.purchaseRequest.mappers.PurchaseRequestMapper;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestItemDetailRepository;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
@EnableAsync
public class PurchaseRequestService {
    private final OfferItemRepository offerItemRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestMapper purchaseRequestMapper ;
    private final PurchaseRequestItemDetailRepository purchaseRequestItemDetailRepository;
    private final OfferRepository offerRepository;
    private final ItemRepository itemRepository;
//    private final SupplierOfferService supplierOfferService;
    private final EmailService emailService;

    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository,
                                  PurchaseRequestMapper purchaseRequestMapper, OfferRepository offerRepository, ItemRepository itemRepository, EmailService emailService,
                                  OfferItemRepository offerItemRepository, PurchaseRequestItemDetailRepository purchaseRequestItemDetailRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestMapper = purchaseRequestMapper;
        this.offerRepository = offerRepository;
        this.itemRepository = itemRepository;
//        this.supplierOfferService = supplierOfferService;
        this.emailService = emailService;
        this.offerItemRepository = offerItemRepository;
        this.purchaseRequestItemDetailRepository = purchaseRequestItemDetailRepository;
    }

    public PurchaseRequestDto createPurchaseRequest(PurchaseRequestDto purchaseRequest) throws ProcureException {
        log.info("creating PR....");
        PurchaseRequest purchaseRequest1 = PurchaseRequestMapper.INSTANCE.toEntity(purchaseRequest);
        purchaseRequest1.setPurchaseRequestTitle(purchaseRequest1.getPurchaseRequestTitle());
        purchaseRequest1.setDueDate(purchaseRequest.getDueDate());
        purchaseRequest1.setApprovalStatus(ApprovalStatus.PENDING);
        Set<Item> items = new HashSet<>(purchaseRequest1.getItems());
        purchaseRequest1.setItems(new HashSet<>(items));
        Set<Supplier> suppliers = new HashSet<>(purchaseRequest1.getSuppliers());
        purchaseRequest1.setSuppliers(new HashSet<>(suppliers));
        PurchaseRequest savedRequest = purchaseRequestRepository.save(purchaseRequest1);
        List<PurchaseRequestItemDetail> purchaseRequestItemDetails = createOfferForPurchaseRequest2(savedRequest.getPurchaseRequestId(),purchaseRequest.getItemDetails());
        // Associate the offer with the purchase request
        savedRequest.setItemDetails(purchaseRequestItemDetails);
//        sendApprovalEmailToSuppliers(savedRequest.getPurchaseRequestId());
        // Additional logic or validation can be added here before saving
        return PurchaseRequestMapper.INSTANCE.toDto(savedRequest);
    }
    @Async
    public void sendApprovalEmailToSuppliers(Long purchaseRequestId) throws ProcureException {
        log.info("Sending order approval emails for Purchase Request with ID: {}", purchaseRequestId);

        if (purchaseRequestId == null) {
            throw ProcureException.builder().message("Purchase request ID not found").metadata("id").build();
        }
        Optional<PurchaseRequest> optionalPurchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);

        if (optionalPurchaseRequest.isPresent()) {
            PurchaseRequest purchaseRequest = optionalPurchaseRequest.get();

            String approvalLinkBase = "http://localhost:8081/api/v1/purchase-order/approve/";

            for (Supplier supplier : purchaseRequest.getSuppliers()) {
                String approvalLink = approvalLinkBase + purchaseRequest.getPurchaseRequestId();
                // Assuming you have a method to email the supplier
                String subject = "New Purchase Request";
                String text = "Dear Supplier,\n\n"
                        + "A purchase request has been sent to you. Please make the best offer.\n\n"
                        + "Purchase Request Title: " + purchaseRequest.getPurchaseRequestTitle() + "\n"
                        + "Due Date: " + purchaseRequest.getDueDate() + "\n"
                        + "Follow this link: " + approvalLink + "\n"
                        + "\n\nBest Regards,\nProcureSwift Company";

                // Send the email
                emailService.sendEmail(supplier.getEmail(), subject, text);
                log.info("Email sent to Supplier: {} for Purchase Request ID: {}", supplier.getVendorId(), purchaseRequestId);
            }

            // Update the purchase order status or perform any other necessary actions
            purchaseRequest.setApprovalStatus(ApprovalStatus.ISSUED); // You can set the appropriate status

            // Save the updated purchase order
            PurchaseRequest savedPurchaseRequest = purchaseRequestRepository.save(purchaseRequest);

            log.info("All approval emails sent for Purchase Request ID: {}", purchaseRequestId);

        } else {
            // Handle the case where the purchase request with the given ID is not found
            log.info("Did not send emails because Purchase Request with ID {} not found", purchaseRequestId);
            throw ProcureException.builder().message("An error occurred").metadata("email sending").build();
        }
    }
    public Offer createOfferForPurchaseRequest(Long purchaseRequestId, MultiOfferDto multiOfferDto) {
        Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
        if (purchaseRequestOptional.isEmpty()) {
            throw new EntityNotFoundException("Purchase request not found");
        }
        PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
        // Create a single offer for the entire purchase request
        Offer singleOffer = Offer.builder()
                .purchaseRequest(purchaseRequest)
                .build();
        Set<OfferItem> offerItems = new HashSet<>();
        for(Supplier supplier : purchaseRequest.getSuppliers()){
            for (OfferDto offerDto : multiOfferDto.getOfferDtoSet()) {
                for (OfferItemDto itemDto : offerDto.getItemDtoSet()) {
                    for(Item item: purchaseRequest.getItems()){
                        OfferItem offerItem = OfferItem.builder()
                                .item(item)
                                .offer(singleOffer)
                                .offerUnitPrice(itemDto.getOfferUnitPrice())
                                .offerTotalPrice(itemDto.getOfferUnitPrice())  // You may want to adjust this calculation
                                .build();
                        offerItems.add(offerItem);
                    }
                }
            }
        }

        // Set all the items to the single offer
        singleOffer.setOfferItems(offerItems);
        return offerRepository.save(singleOffer);
    }
    public List<PurchaseRequestItemDetail> createOfferForPurchaseRequest2(Long purchaseRequestId, List<PurchaseRequestItemDetail> offerDetails) {
        Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
        if (purchaseRequestOptional.isEmpty()) {
            throw new EntityNotFoundException("Purchase request not found");
        }
        PurchaseRequest purchaseRequest = purchaseRequestOptional.get();

        // Create a list to store all the created offer details
        List<PurchaseRequestItemDetail> createdOfferDetails = new ArrayList<>();

        // Iterate through each supplier in the purchase request
        for (Supplier supplier : purchaseRequest.getSuppliers()) {
            // Create a single offer detail for the entire purchase request for the current supplier
            // Create a set to store items in the offer
            // Iterate through each item in the purchase request
            for (Item item : purchaseRequest.getItems()) {
                PurchaseRequestItemDetail singleOfferDetail = PurchaseRequestItemDetail.builder()
                        .purchaseRequest(purchaseRequest)
                        .supplier(supplier)
                        .item(item)
                        .offerUnitPrice(BigDecimal.ZERO) // Set default offer unit price
                        .build();
                PurchaseRequestItemDetail createdOfferDetail = purchaseRequestItemDetailRepository.save(singleOfferDetail);
                createdOfferDetails.add(createdOfferDetail);
                // Create a new item in the offer detail with the specified offer unit price
            }
            // Set all the items to the single offer detail
            // Save the single offer detail with associated items
            // Add the created offer detail to the list
        }
        return createdOfferDetails;
    }
//    public List<PurchaseRequestItemDetail> editOfferUnitPrices(Long purchaseRequestId, List<PurchaseRequestItemDetail> itemDetails) {
//        Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
//        if (purchaseRequestOptional.isEmpty()) {
//            throw new EntityNotFoundException("Purchase request not found");
//        }
//        PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
//
//        // Create a map to store items for each supplier
//        Map<Supplier, Set<PurchaseRequestItemDetail>> itemsBySupplier = new HashMap<>();
//
//        // Iterate through the list and organize items by supplier
//        for (PurchaseRequestItemDetail updatedItemDetail : itemDetails) {
//            Supplier supplier = updatedItemDetail.getSupplier();
//
//            // Create a set for the supplier if it doesn't exist in the map
//            itemsBySupplier.computeIfAbsent(supplier, k -> new HashSet<>());
//
//            // Find the corresponding item in the purchase request
//            Optional<PurchaseRequestItemDetail> existingItemDetailOptional = purchaseRequest
//                    .getItemDetails()
//                    .stream()
//                    .filter(itemDetail ->
//                            Objects.equals(itemDetail.getItem().getItemId(), updatedItemDetail.getItem().getItemId())
//                                    && Objects.equals(itemDetail.getSupplier().getVendorId(), supplier.getVendorId()))
//                    .findFirst();
//            if (existingItemDetailOptional.isPresent()) {
//                PurchaseRequestItemDetail existingItemDetail = existingItemDetailOptional.get();
//                // Perform validation or any other business logic as needed
//                // Update offer unit price
//                existingItemDetail.setOfferUnitPrice(updatedItemDetail.getOfferUnitPrice());
//
//                // Add the item detail to the set for the supplier
//                itemsBySupplier.get(supplier).add(existingItemDetail);
//            } else {
//                // Handle the case where the item detail is not found in the purchase request
//                // You may throw an exception, log a warning, or handle it based on your requirements
//                throw new EntityNotFoundException("Item detail not found in the purchase request");
//            }
//        }
//
//        // Save all updated item details in the database
//        purchaseRequestItemDetailRepository.saveAll(itemDetails);
//
//        // Return the updated item details
//        return itemDetails;
//    }
    public List<PurchaseRequestItemDetail> editOfferUnitPrices(Long purchaseRequestId, List<PurchaseRequestItemDetail> itemDetails) {
        Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
        if (purchaseRequestOptional.isEmpty()) {
            throw new EntityNotFoundException("Purchase request not found");
        }
        PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
        // Iterate through the list and update the offer unit prices in the database
        for (PurchaseRequestItemDetail updatedItemDetail : itemDetails) {
            // Find the corresponding item in the purchase request
            Optional<PurchaseRequestItemDetail> existingItemDetailOptional = purchaseRequest
                    .getItemDetails()
                    .stream()
                    .filter(itemDetail ->
                            Objects.equals(itemDetail.getItem().getItemId(), updatedItemDetail.getItem().getItemId())
                                    && Objects.equals(itemDetail.getSupplier().getVendorId(), updatedItemDetail.getSupplier().getVendorId()))
                    .findFirst();
            if (existingItemDetailOptional.isPresent()) {
                PurchaseRequestItemDetail existingItemDetail = existingItemDetailOptional.get();
                // Perform validation or any other business logic as needed
                // Update offer unit price
                existingItemDetail.setOfferUnitPrice(updatedItemDetail.getOfferUnitPrice());
            } else {
                // Handle the case where the item detail is not found in the purchase request
                // You may throw an exception, log a warning, or handle it based on your requirements
                throw new EntityNotFoundException("Item detail not found in the purchase request");
            }
        }
        // Save all updated item details in the database
        purchaseRequestItemDetailRepository.saveAll(itemDetails);
        // Return the updated item details
        return itemDetails;
    }

    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll();
    }

    // Get a purchase request by ID
    public Optional<PurchaseRequestDto> getPurchaseRequestById(Long purchaseRequestId) {
        Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);
        return purchaseRequest.map(purchaseRequestMapper::toDto);
    }

}
