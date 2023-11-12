package com.rose.procurement.items.controller;

import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.request.ItemRequest;
import com.rose.procurement.items.service.ItemService;
import org.springframework.http.MediaType;
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
    public ItemDto createItem(@RequestBody ItemDto itemRequest) {
        return itemService.createItem(itemRequest);
    }

    @GetMapping
    public List<ItemDto> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable("id") String itemId) {
        return itemService.getItemById(itemId);
    }
    @DeleteMapping("{id}")
    public String deleteItem(@PathVariable("id") String itemId){
        return itemService.deleteItem(itemId);
    }
}
