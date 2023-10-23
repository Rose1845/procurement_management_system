package com.rose.procurement.items.service;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    public Item createItem(Item itemRequest) {
        Optional<Category> category = categoryRepository.findByCategoryId(itemRequest.getCategory().getCategoryId());
        if(category.isEmpty()){
            throw  new IllegalStateException("category with id do not esist");
        }
        Item item = Item.builder()
                .itemId(itemRequest.getItemId())
                .itemDescription(itemRequest.getItemDescription())
                .itemNumber(itemRequest.getItemNumber())
                .itemName(itemRequest.getItemName())
                .quantity(itemRequest.getQuantity())
                .unitPrice(itemRequest.getUnitPrice())
                .category(category.get())
                .build();
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }
}
