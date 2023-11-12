package com.rose.procurement.supplier.services;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierDto;
import com.rose.procurement.supplier.mappers.SupplierMapper;
import com.rose.procurement.supplier.repository.SupplierRepository;
import com.rose.procurement.supplier.request.SupplierRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository,
                           SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    public SupplierDto createSupplier(SupplierDto supplierRequest) {
        Supplier supplier = Supplier
                .builder()
                .address(supplierRequest.getAddress())
                .termsAndConditions(supplierRequest.getTermsAndConditions())
                .email(supplierRequest.getEmail())
                .name(supplierRequest.getName())
                .phoneNumber(supplierRequest.getPhoneNumber())
                .contactInformation(supplierRequest.getContactInformation())
                .contactPerson(supplierRequest.getContactPerson())
                .paymentType(supplierRequest.getPaymentType())
                .build();
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(savedSupplier);
    }
    public List<Supplier> getAllSuppliers(){

        return new ArrayList<>(supplierRepository.findAll());
    }

    public Supplier getSupplierById(Long vendorId) {
        return supplierRepository.findByVendorId(vendorId)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + vendorId));
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
