package com.rose.procurement.offer;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/offer-items")
public class OfferItemController {
    private final OfferItemRepository offerItemRepository;
    private final OfferRepository offerRepository;
    private final SupplierService supplierService;
    private final  OfferItemService offerItemService;
  @GetMapping
  public List<OfferItem> getOfferItem(){
      return offerItemRepository.findAll();
  }
  @GetMapping("/{id}")
  public Optional<OfferItem> getOffer(@PathVariable("id") Long id){
//      Optional<Offer> offer = offerRepository.findById(id);
      return offerItemRepository.findById(id);
  }
    @GetMapping("/by-offer/{id}")
    public List<OfferItem> getOfferItemsByOfferId(@PathVariable("id") Long offerId) {
      return offerItemService.findOfferItemsByOffer(offerId);
    }
}
