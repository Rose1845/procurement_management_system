package com.rose.procurement.items.controller;

import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/items")
public class ItemController {
    private final ItemService itemService;
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    @PostMapping
    public ItemDto createItem(@RequestBody @Valid ItemDto itemRequest) {
        return itemService.createItem(itemRequest);
    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") String itemId) {
        return itemService.getItemById(itemId);
    }
    @DeleteMapping("{id}")
    public String deleteItem(@PathVariable("id") String itemId){
        return itemService.deleteItem(itemId);
    }
}
