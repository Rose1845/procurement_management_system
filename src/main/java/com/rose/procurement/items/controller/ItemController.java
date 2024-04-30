package com.rose.procurement.items.controller;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.items.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    @GetMapping("/export/items")
    public ResponseEntity<ByteArrayResource> exportCategories() throws IOException {
        byte[] csvData = itemService.exportItemsToCsv();

        ByteArrayResource resource = new ByteArrayResource(csvData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=items.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvData.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
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
    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority({'ADMIN','EMPLOYEE'})")
    public Item updateItem(@PathVariable("id") String itemId, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(itemId,itemDto);
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

    @GetMapping("/pagination/items/")
    public Page<Item> findAllItems(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String category
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Item> filteredItems = null;
        if (itemName != null && !itemName.isEmpty() && category != null && !category.isEmpty()) {
            // Search by item name and category name with pagination and sorting
            filteredItems = itemRepository.findByItemNameContainingAndCategory_CategoryName(itemName, category, pageable);
        } else if (itemName != null && !itemName.isEmpty()) {
            // Search by item name with pagination and sorting
            filteredItems = itemRepository.findByItemNameContaining(itemName, pageable);
        } else if (category != null && !category.isEmpty()) {
            // Search by category name with pagination and sorting
            filteredItems = itemRepository.findByCategory_CategoryName(category, pageable);
        } else {
            // No filters applied, return all items with pagination and sorting
            filteredItems = itemRepository.findAll(pageable);
        }
        return filteredItems;
    }

}
