package com.rose.procurement.items.service;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.items.request.ItemRequest;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    public Item createItem(ItemRequest itemRequest) {
        Optional<Category> category = categoryRepository.findByCategoryId(itemRequest.getCategory().getCategoryId());
        if(category.isEmpty()){
            throw  new IllegalStateException("category with id do not esist");
        }
        Optional<Supplier> supplier = supplierRepository.findByVendorId(itemRequest.getSupplier().getVendorId());
        if(supplier.isEmpty()){
            throw  new IllegalStateException("category with id do not esist");
        }
        Item item = Item.builder()
                .itemDescription(itemRequest.getItemDescription())
                .itemNumber(itemRequest.getItemNumber())
                .itemName(itemRequest.getItemName())
                .quantity(itemRequest.getQuantity())
                .unitPrice(itemRequest.getUnitPrice())
                .category(category.get())
                .supplier(supplier.get())
                .build();
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(String itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
    }
    public String  deleteItem(String itemId){
        Optional<Item> item = itemRepository.findById(itemId);
        if(item.isPresent()){
            itemRepository.deleteById(itemId);
        }
        return "item deleted successfully";
    }
}
