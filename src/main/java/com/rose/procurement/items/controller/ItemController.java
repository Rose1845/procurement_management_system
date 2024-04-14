package com.rose.procurement.items.controller;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.items.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;

    public ItemController(ItemService itemService, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
    }

    @PostMapping("/create")
//    @PreAuthorize("hasAnyAuthority({'ADMIN','EMPLOYEE'})")
    public ItemDto createItem(@RequestBody @Valid ItemDto itemRequest) throws ProcureException {
        return itemService.createItem(itemRequest);
    }

    @GetMapping("/item-details/{id}")
    public Optional<ItemDto> getItemDetails(@PathVariable("id") String itemId) {
        return itemService.getItemDetails(itemId);
    }

    @GetMapping("/all-by-pagination")
    public Page<Item> findAllPurchaseOrders1(@RequestParam(name = "page", defaultValue = "0") int page,
                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE'})")
    public ItemDto getItemById(@PathVariable("id") String itemId) {
        return itemService.getItemById(itemId);
    }

    @DeleteMapping("{id}")
    public String deleteItem(@PathVariable("id") String itemId) {
        return itemService.deleteItem(itemId);
    }

    @PostMapping("/{purchaseRequestId}")
    public Item addPurchaseRequestItem(
            @PathVariable Long purchaseRequestId,
            @RequestBody Item purchaseRequestItem) {
        return itemService.addPurchaseRequestItem(purchaseRequestId, purchaseRequestItem);
    }
}
