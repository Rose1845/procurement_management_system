package com.rose.procurement.items.mappers;

import com.rose.procurement.category.entity.Category;
import com.rose.procurement.category.mappers.CategoryMapper;
import com.rose.procurement.category.repository.CategoryRepository;
import com.rose.procurement.items.dtos.ItemDto;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.mappers.SupplierMapper;
import com.rose.procurement.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemMapperImp implements ItemMapper{

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public ItemMapperImp(SupplierRepository supplierRepository,
                         SupplierMapper supplierMapper,
                         CategoryRepository categoryRepository,
                         CategoryMapper categoryMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Item toEntity(ItemDto itemDto) {
         return null;
//        Item.builder()
//                .itemName(itemDto.getItemName())
//                .itemNumber(itemDto.getItemNumber())
//                .itemDescription(itemDto.getItemDescription())
//                .quantity(itemDto.getQuantity())
//                .unitPrice(itemDto.getUnitPrice())
//                .category(itemDto.getCategory())
//                .supplier(itemDto.getSupplier())
//                .build();
    }

//    @Override
//    public ItemDto toDto(Item item, Supplier supplier, Category category) {
////        Optional<Supplier> supplier1 = supplierRepository.findByVendorId(item.getSupplier().getVendorId());
////        Optional<Category> category1 = categoryRepository.findByCategoryId(item.getCategory().getCategoryId());
//        ItemDto itemDto = new ItemDto();
//        itemDto.setItemDescription(item.getItemDescription());
//        itemDto.setItemName(item.getItemName());
//        itemDto.setQuantity(item.getQuantity());
//        itemDto.setItemNumber(item.getItemNumber());
//        itemDto.setUnitPrice(item.getUnitPrice());
//        if(supplier != null){
//            itemDto.setSupplierVendorId(supplier.getVendorId());
//        }
//        if(category != null){
//            itemDto.setCategoryCategoryId(category.getCategoryId());
//        }
//        return itemDto;
//
////        ItemDto item1 = ItemDto.builder()
////                .itemDescription(item.getItemDescription())
////                .unitPrice(item.getUnitPrice())
////                .itemName(item.getItemName())
////                .category()
////
////                .build();
//    }
//
    @Override
    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .itemName(item.getItemName())
                .itemNumber(item.getItemNumber())
                .itemDescription(item.getItemDescription())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .category(item.getCategory())
                .supplier(item.getSupplier())
                .build();
    }

    @Override
    public Item partialUpdate(ItemDto itemDto, Item item) {
        return null;
    }
}
