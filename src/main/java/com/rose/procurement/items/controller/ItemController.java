package com.rose.procurement.items.controller;

import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/items")
public class ItemController {
    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority({'ADMIN','EMPLOYEE'})")
    public ItemDto createItem(@RequestBody @Valid ItemDto itemRequest) {
        return itemService.createItem(itemRequest);
    }

    @GetMapping("/item-details/{id}")
    public Optional<ItemDto> getItemDetails(@PathVariable("id") String itemId){
        return itemService.getItemDetails(itemId);
    }
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE'})")
    public ItemDto getItemById(@PathVariable("id") String itemId) {
        return itemService.getItemById(itemId);
    }
    @DeleteMapping("{id}")
    public String deleteItem(@PathVariable("id") String itemId){
        return itemService.deleteItem(itemId);
    }

    @PostMapping("/{purchaseRequestId}")
    public Item addPurchaseRequestItem(
            @PathVariable Long purchaseRequestId,
            @RequestBody Item purchaseRequestItem) {
        return itemService.addPurchaseRequestItem(purchaseRequestId, purchaseRequestItem);
    }
}
