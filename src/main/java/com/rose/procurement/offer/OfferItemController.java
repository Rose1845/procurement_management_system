package com.rose.procurement.offer;

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
  @GetMapping
  public List<OfferItem> getOfferItem(){
      return offerItemRepository.findAll();
  }
  @GetMapping("/{id}")
  public Optional<OfferItem> getOffer(@PathVariable("id") Long id){
//      Optional<Offer> offer = offerRepository.findById(id);
      return offerItemRepository.findById(id);
  }

}
