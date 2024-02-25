package com.rose.procurement.offer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/create")
    public Offer createOfferForPurchaseRequest(
            @RequestParam Long purchaseRequestId,
            @RequestBody MultiOfferDto offerItemDto
    ) {
        // Assuming you have a method to retrieve the Supplier based on supplierId
        return offerService.createOfferForPurchaseRequest(purchaseRequestId, offerItemDto);
    }
    @GetMapping("/{id}")
    public Optional<Offer> getOffer(@PathVariable("id") Long id){
        return offerService.getOffer(id);
    }


}
