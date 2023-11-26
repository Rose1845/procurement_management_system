//package com.rose.procurement.supplier.mappers;
//
//import com.rose.procurement.supplier.entities.Supplier;
//import com.rose.procurement.supplier.entities.SupplierDto;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SupplierMapperImpl implements SupplierMapper{
//    @Override
//    public Supplier toEntity(SupplierDto supplierDto) {
//        Supplier supplier = Supplier
//                .builder()
//                .address(supplierDto.getAddress())
//                .termsAndConditions(supplierDto.getTermsAndConditions())
//                .email(supplierDto.getEmail())
//                .name(supplierDto.getName())
//                .phoneNumber(supplierDto.getPhoneNumber())
//                .contactInformation(supplierDto.getContactInformation())
//                .contactPerson(supplierDto.getContactPerson())
//                .paymentType(supplierDto.getPaymentType())
//                .build();
//        BeanUtils.copyProperties(supplier,supplierDto);
//        return supplier;
//    }
//
//    @Override
//    public SupplierDto toDto(Supplier supplier) {
//        SupplierDto supplierDto =  SupplierDto
//                .builder()
//                .address( supplier.getAddress())
//                .termsAndConditions( supplier.getTermsAndConditions())
//                .email( supplier.getEmail())
//                .name( supplier.getName())
//                .phoneNumber( supplier.getPhoneNumber())
//                .contactInformation( supplier.getContactInformation())
//                .contactPerson( supplier.getContactPerson())
//                .paymentType( supplier.getPaymentType())
//                .build();
//        BeanUtils.copyProperties(supplierDto,supplier);
//        return supplierDto;
//    }
//
//    @Override
//    public Supplier partialUpdate(SupplierDto supplierDto, Supplier supplier) {
//        return null;
//    }
//}
