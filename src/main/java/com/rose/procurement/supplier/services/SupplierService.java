package com.rose.procurement.supplier.services;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.supplier.mappers.SupplierMapper;
import com.rose.procurement.supplier.repository.SupplierRepository;
import com.rose.procurement.supplier.request.SupplierRequest;
import com.rose.procurement.utils.address.Address;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository
                          ) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierDto createSupplier(SupplierDto supplierRequest) {
        Address address = new Address();
        address.setBox(supplierRequest.getAddress().getBox());
        address.setCity(supplierRequest.getAddress().getCity());
        address.setCountry(supplierRequest.getAddress().getCountry());
        address.setLocation(supplierRequest.getAddress().getLocation());
        Supplier supplier = SupplierMapper.MAPPER.toEntity(supplierRequest);
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setTermsAndConditions(supplierRequest.getTermsAndConditions());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setName(supplierRequest.getName());
        supplier.setPhoneNumber(supplierRequest.getPhoneNumber());
        supplier.setAddress(address);
        supplier.setContactInformation(supplierRequest.getContactInformation());
        supplier.setContactPerson(supplierRequest.getContactPerson());
        supplier.setPaymentType(supplierRequest.getPaymentType());
        Supplier savedSupplier = supplierRepository.save(supplier);
        return SupplierMapper.MAPPER.toDto(savedSupplier);
    }
    public List<Supplier> getAllSuppliers(){
        return new ArrayList<>(supplierRepository.findAll());
    }

    public Supplier getSupplierById(Long vendorId) {
        Supplier supplier =supplierRepository.findById(vendorId).get();
        //return UserMapper.mapToUserDto(user);
        //return modelMapper.map(user, UserDto.class);
        return supplier;
//        return SupplierMapper.MAPPER.toDto(supplier);
//        return supplierRepository.findByVendorId(vendorId)
//                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + vendorId));
    }

    public Supplier updateSupplier(Long vendorId,SupplierRequest supplierRequest){
        Supplier supplier1 = supplierRepository.findByVendorId(vendorId).orElseThrow(()-> new IllegalStateException("supplier do not exist"));
        supplier1.setName(supplierRequest.getName());
        supplier1.setAddress(supplierRequest.getAddress());
        supplier1.setEmail(supplierRequest.getEmail());
        supplier1.setContactPerson(supplierRequest.getContactPerson());
        supplier1.setPaymentType(supplierRequest.getPaymentTerm());
        supplier1.setContactInformation(supplierRequest.getContactInformation());
        supplier1.setPhoneNumber(supplierRequest.getPhoneNumber());
        supplier1.setTermsAndConditions(supplierRequest.getTermsAndConditions());
        return supplierRepository.save(supplier1);
    }

    public String deleteSupplier(Long vendorId){
        Optional<Supplier> supplier = supplierRepository.findById(vendorId);
        if(supplier.isPresent()){
            supplierRepository.deleteById(vendorId);
        }else {
            return "suplier with id:" + vendorId + "do not exists";
        }
        return "supplier deleted successfully";
    }

}
