package com.rose.procurement.items.service;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.items.mappers.ItemMapper;
import com.rose.procurement.items.repository.ItemRepository;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.repository.PurchaseRequestRepository;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.supplier.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ItemMapper itemMapper;
    private final PurchaseRequestRepository purchaseRequestRepository;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository,
                       ItemMapper itemMapper, PurchaseRequestRepository purchaseRequestRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.itemMapper = itemMapper;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    public ItemDto createItem(ItemDto itemRequest) throws ProcureException {
        if (itemRepository.existsByItemName(itemRequest.getItemName())) {
            throw ProcureException.builder().message("Item Name already exist").build();
        }
        log.info("Received ItemDto: {}", itemRequest);
        Optional<Category> category = categoryRepository.findById(itemRequest.getCategoryId());
        Item item1 = ItemMapper.MAPPER.toEntity(itemRequest);
        item1.setItemName(itemRequest.getItemName());
        item1.setQuantity(itemRequest.getQuantity());
        item1.setUnitPrice(itemRequest.getUnitPrice());
        category.ifPresent(item1::setCategory);
        // Calculate and set the total price
        BigDecimal totalPrice = BigDecimal.valueOf(item1.getQuantity()).multiply(item1.getUnitPrice());
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

    public String deleteItem(String itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            itemRepository.deleteById(itemId);
        }
        return "item deleted successfully";
    }

    public Optional<ItemDto> getItemDetails(String itemId) {
        Optional<Item> item = itemRepository.findItemDetailsById(itemId);
        return item.map(itemMapper::toDto);
    }

    public Item addPurchaseRequestItem(Long purchaseRequestId, Item purchaseRequestItem) {
        // Retrieve the purchase request by ID
        PurchaseRequest purchaseRequest = purchaseRequestRepository.findById(purchaseRequestId)
                .orElseThrow(() -> new RuntimeException("Purchase request not found"));

        // Set the relationship and save the purchase request item
        purchaseRequestItem.setPurchaseRequests((Set<PurchaseRequest>) purchaseRequest);
        purchaseRequest.getItems().add(purchaseRequestItem);

        return itemRepository.save(purchaseRequestItem);
    }
    public Item updateItem(String itemId, ItemDto supplierRequest) {
        Item supplier1 = itemRepository.findById(itemId).orElseThrow(() -> new IllegalStateException("item do not exist"));
        supplier1.setItemName(supplierRequest.getItemName());
        supplier1.setQuantity(supplierRequest.getQuantity());
        supplier1.setItemDescription(supplierRequest.getItemDescription());
        supplier1.setUnitPrice(supplierRequest.getUnitPrice());
        BigDecimal totalPrice = BigDecimal.valueOf(supplier1.getQuantity()).multiply(supplier1.getUnitPrice());
        supplier1.setTotalPrice(totalPrice);
        Optional<Category> category =  categoryRepository.findById(supplierRequest.getCategoryId());
        supplier1.setCategory(category.get());
        return itemRepository.save(supplier1);
    }

}
