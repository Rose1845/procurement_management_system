package com.rose.procurement.items.service;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.mappers.CategoryMapper;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.mappers.ItemMapper;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.mappers.SupplierMapper;
import com.rose.procurement.supplier.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository
                    ) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    public ItemDto createItem(ItemDto itemRequest) {
        log.info("Received ItemDto: {}", itemRequest);
        Optional<Category> category = categoryRepository.findById(itemRequest.getCategoryId());
        Optional<Supplier> supplier = supplierRepository.findById(itemRequest.getVendorId());
        Item item1 = ItemMapper.MAPPER.toEntity(itemRequest);
        item1.setItemNumber(itemRequest.getItemNumber());
        item1.setItemName(itemRequest.getItemName());
        item1.setQuantity(itemRequest.getQuantity());
        item1.setUnitPrice(itemRequest.getUnitPrice());
        category.ifPresent(item1::setCategory);
        supplier.ifPresent(item1::setSupplier);
        // Calculate and set the total price
        double totalPrice = item1.getQuantity() * item1.getUnitPrice();
        item1.setTotalPrice(totalPrice);
        Item savedItem = itemRepository.save(item1);
        return ItemMapper.MAPPER.toDto(savedItem);
    }

    public List<Item> getAllItems() {

        return new ArrayList<>(itemRepository.findAll());
//        return itemRepository.findAll().stream().map(ItemMapper.MAPPER::toDto).collect(Collectors.toList());
    }

    public ItemDto getItemById(String itemId) {
        Item item = itemRepository.findById(itemId).get();
        return ItemMapper.MAPPER.toDto(item);
//        return itemRepository.findById(itemId)
//                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
    }
    public String  deleteItem(String itemId){
        Optional<Item> item = itemRepository.findById(itemId);
        if(item.isPresent()){
            itemRepository.deleteById(itemId);
        }
        return "item deleted successfully";
    }
}
