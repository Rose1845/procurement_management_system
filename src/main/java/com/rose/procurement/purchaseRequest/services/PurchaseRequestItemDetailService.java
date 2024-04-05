package com.rose.procurement.purchaseRequest.services;

import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestItemDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseRequestItemDetailService {

    private final PurchaseRequestItemDetailRepository itemDetailRepository;

    public List<PurchaseRequestItemDetail> getAllItemDetails() {
        return itemDetailRepository.findAll();
    }

    public PurchaseRequestItemDetail getItemDetailById(Long id) {
        return itemDetailRepository.findById(id).orElse(null);
    }

    //    public List<PurchaseRequestItemDetail> getItemDetailsByPurchaseRequestId(Long purchaseRequest) {
//        return itemDetailRepository.findByPurchaseRequest(purchaseRequest);
//    }
    public PurchaseRequestItemDetail createItemDetail(PurchaseRequestItemDetail itemDetail) {
        return itemDetailRepository.save(itemDetail);
    }

    public void deleteItemDetail(Long id) {
        itemDetailRepository.deleteById(id);
    }

    public List<PurchaseRequestItemDetail> editOfferUnitPrices(List<PurchaseRequestItemDetail> itemDetails) {
        // Iterate through the list and update the offer unit prices in the database
        // Perform validation or any other business logic as needed
        // Update offer unit price in the database
        itemDetailRepository.saveAll(itemDetails);
        // Return the updated item details
        return itemDetails;
    }

    public List<PurchaseRequestItemDetail> getDetailsBySupplierId(String supplierId) {
        return itemDetailRepository.findBySupplier_VendorId(supplierId);
    }

    public List<PurchaseRequestItemDetail> getDetailsByRequestId(Long purchaseRequestId) {
        return itemDetailRepository.findByPurchaseRequest_PurchaseRequestId(purchaseRequestId);
    }
}


