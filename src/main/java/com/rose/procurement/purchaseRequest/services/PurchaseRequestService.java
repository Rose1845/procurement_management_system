package com.rose.procurement.purchaseRequest.services;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.email.service.EmailService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.offer.*;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import com.rose.procurement.purchaseRequest.mappers.PurchaseRequestMapper;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@EnableAsync
public class PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestMapper purchaseRequestMapper ;
    private final OfferRepository offerRepository;
    private final ItemRepository itemRepository;
//    private final SupplierOfferService supplierOfferService;
    private final EmailService emailService;

    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository,
                                  PurchaseRequestMapper purchaseRequestMapper, OfferRepository offerRepository, ItemRepository itemRepository, EmailService emailService) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestMapper = purchaseRequestMapper;
        this.offerRepository = offerRepository;
        this.itemRepository = itemRepository;
//        this.supplierOfferService = supplierOfferService;
        this.emailService = emailService;
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
//    public void createOffersForPurchaseRequest(Long purchaseRequestId, MultiOfferDto multiOfferDto) {
//        Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
//        if (purchaseRequestOptional.isEmpty()) {
//            throw new EntityNotFoundException("Purchase request not found");
//        }
//
//        PurchaseRequest purchaseRequest = purchaseRequestOptional.get();
//
//        // Create a single offer for the entire purchase request
//        Offer singleOffer = Offer.builder()
//                .purchaseRequest(purchaseRequest)
//                .build();
//
//        Set<OfferItem> offerItems = new HashSet<>();
//        for(Supplier supplier : purchaseRequest.getSuppliers()){
//            for (OfferDto offerDto : multiOfferDto.getOfferDtoSet()) {
//                for (OfferItemDto itemDto : offerDto.getItemDtoSet()) {
//                    Item item = itemRepository.findById(itemDto.getItemId())
//                            .orElseThrow(() -> new EntityNotFoundException("Item not found"));
//                    OfferItem offerItem = OfferItem.builder()
//                            .item(item)
//                            .offer(singleOffer)
//                            .supplier(supplier)  // Set the supplier for each offer item
//                            .offerUnitPrice(itemDto.getOfferUnitPrice())
//                            .offerTotalPrice(itemDto.getOfferUnitPrice())  // You may want to adjust this calculation
//                            .build();
//                    offerItems.add(offerItem);
//                }
//            }
//        }
//
//
//        // Set all the items to the single offer
//        singleOffer.setItems(offerItems);
//        // Save the single offer for the entire purchase request
//        offerRepository.save(singleOffer);
//    }


    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll();
    }

    // Get a purchase request by ID
    public Optional<PurchaseRequestDto> getPurchaseRequestById(Long purchaseRequestId) {
        Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);
        return purchaseRequest.map(purchaseRequestMapper::toDto);
    }

    // Associate a Purchase Order with a Purchase Request
//    public PurchaseRequest createPurchaseOrder(Long purchaseRequestId, PurchaseOrder purchaseOrder) {
//        Optional<PurchaseRequest> optionalPurchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);
//
//        if (optionalPurchaseRequest.isPresent()) {
//            PurchaseRequest purchaseRequest = optionalPurchaseRequest.get();
////            purchaseRequest.setSuppliers(purchaseOrder.ge);
//            return purchaseRequestRepository.save(purchaseRequest);
//        } else {
//            // Handle the case where the purchase request with the given ID is not found
//            throw new EntityNotFoundException("Purchase Request not found with ID: " + purchaseRequestId);
//        }
//    }



}
