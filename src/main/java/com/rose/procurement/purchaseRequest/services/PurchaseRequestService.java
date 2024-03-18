package com.rose.procurement.purchaseRequest.services;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.email.service.EmailService;
import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.QuoteStatus;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestDto;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import com.rose.procurement.purchaseRequest.mappers.PurchaseRequestMapper;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestItemDetailRepository;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
import com.rose.procurement.supplier.entities.Supplier;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseRequestService {
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final PurchaseRequestMapper purchaseRequestMapper ;
    private final PurchaseRequestItemDetailRepository purchaseRequestItemDetailRepository;
    private final ItemRepository itemRepository;
//    private final SupplierOfferService supplierOfferService;
    private final EmailService emailService;

    public PurchaseRequestService(PurchaseRequestRepository purchaseRequestRepository,
                                  PurchaseRequestMapper purchaseRequestMapper,ItemRepository itemRepository, EmailService emailService,
                                  PurchaseRequestItemDetailRepository purchaseRequestItemDetailRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.purchaseRequestMapper = purchaseRequestMapper;
        this.itemRepository = itemRepository;
//        this.supplierOfferService = supplierOfferService;
        this.emailService = emailService;
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
    public String sendApprovalEmailToSuppliers(Long purchaseRequestId) throws ProcureException {
        log.info("Sending request offer  emails for Purchase Request with ID: {}", purchaseRequestId);
        if (purchaseRequestId == null) {
            throw ProcureException.builder().message("Purchase request ID not found").metadata("id").build();
        }

        Optional<PurchaseRequest> optionalPurchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);

        if (optionalPurchaseRequest.isPresent()) {
            PurchaseRequest purchaseRequest = optionalPurchaseRequest.get();

            String approvalLinkBase = "/api/v1/purchase-request/" + purchaseRequest.getPurchaseRequestId() + "/edit2-offer-unit-prices2?supplierId=";

            for (Supplier supplier : purchaseRequest.getSuppliers()) {
                String approvalLink = approvalLinkBase + supplier.getVendorId();
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
        return "Email Sent";
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
                        .quoteStatus(QuoteStatus.Waiting_for_offer)
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
    public void acceptOffer(Long purchaseRequestId, String supplierId) {
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase request not found"));
        purchaseRequest.setApprovalStatus(ApprovalStatus.COMPLETED);

        boolean offerAccepted = false;

        // Iterate over item details to find the supplier's offers and accept them
        for (PurchaseRequestItemDetail itemDetail : purchaseRequest.getItemDetails()) {
            if (itemDetail.getSupplier().getVendorId().equals(supplierId)) {
                itemDetail.setQuoteStatus(QuoteStatus.BUYER_ACCEPTED);
                purchaseRequestItemDetailRepository.save(itemDetail);
                offerAccepted = true;
            }
        }

        // If an offer is accepted, notify other suppliers
        if (offerAccepted) {
            sendCancellationEmailsToOtherSuppliers(purchaseRequest, supplierId);
        }
    }
    private void sendCancellationEmailsToOtherSuppliers(PurchaseRequest purchaseRequest, String acceptedSupplierId) {
        Set<Supplier> otherSuppliers = purchaseRequest.getSuppliers().stream()
                .filter(supplier -> !Objects.equals(supplier.getVendorId(), acceptedSupplierId))
                .collect(Collectors.toSet());
        for (Supplier supplier : otherSuppliers) {
            log.info("sending email");

            // Check if email, subject, and body are not null before sending the email
            if (supplier.getEmail() != null && purchaseRequest.getPurchaseRequestId() != null) {
                String emailSubject = "Offer Cancellation Notice";
                String emailBody = "Your offer for the purchase request " + purchaseRequest.getPurchaseRequestId() + " has been cancelled.";
                emailService.sendEmail(supplier.getEmail(), emailSubject, emailBody);
                log.info("email sent!");
            } else {
                log.info("error occured");
                // Handle the case where either email or purchaseRequestId is null
                // This could include logging an error or skipping the email sending
                // For example:
                // logger.error("Failed to send cancellation email to supplier: Email or purchase request ID is null.");
            }
        }
    }



    public List<PurchaseRequestItemDetail> editOfferUnitPrices2(Long purchaseRequestId, String supplierId, List<PurchaseRequestItemDetail> itemDetails) {
        Optional<PurchaseRequest> purchaseRequestOptional = purchaseRequestRepository.findById(purchaseRequestId);
        if (purchaseRequestOptional.isEmpty()) {
            throw new EntityNotFoundException("Purchase request not found");
        }

        PurchaseRequest purchaseRequest = purchaseRequestOptional.get();

        // Iterate through each updated item detail
        for (PurchaseRequestItemDetail updatedItemDetail : itemDetails) {
            // Find the corresponding item detail in the purchase request
            Optional<PurchaseRequestItemDetail> existingItemDetailOptional = purchaseRequest.getItemDetails().stream()
                    .filter(itemDetail ->
                            itemDetail.getSupplier() != null && itemDetail.getSupplier().getVendorId().equals(supplierId)
                                    && itemDetail.getItem().getItemId().equals(updatedItemDetail.getItem().getItemId()))
                    .findFirst();

            // If the item detail exists, update its offer unit price
            existingItemDetailOptional.ifPresent(existingItemDetail -> {
                // Update offer unit price
                existingItemDetail.setOfferUnitPrice(updatedItemDetail.getOfferUnitPrice());
            });

        }

        // Save all updated item details associated with the specified supplier in the database
        return purchaseRequestItemDetailRepository.saveAll(purchaseRequest.getItemDetails());
    }

    public List<PurchaseRequest> getAllPurchaseRequests() {
        return purchaseRequestRepository.findAll();
    }

    // Get a purchase request by ID
    public Optional<PurchaseRequestDto> getPurchaseRequestById(Long purchaseRequestId) {
        Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId);
        return purchaseRequest.map(purchaseRequestMapper::toDto);
    }
    public List<PurchaseRequest> findPurchaseRequestsBySupplierId(Long supplierId) {
        return purchaseRequestRepository.findAllBySupplierId(supplierId);
    }

    public Optional<PurchaseRequest> getPurchaseRequestDetailsForSupplier(Long purchaseRequestId, String vendorId) {
        return purchaseRequestRepository.findByPurchaseRequestIdAndSuppliers_VendorId(purchaseRequestId, vendorId);
    }
}
