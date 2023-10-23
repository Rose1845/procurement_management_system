package com.rose.procurement.supplier.services;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import com.rose.procurement.supplier.request.SupplierRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier createSupplier(SupplierRequest supplierRequest) {
        Supplier supplier = Supplier
                .builder()
                .address(supplierRequest.getAddress())
                .termsAndConditions(supplierRequest.getTermsAndConditions())
                .email(supplierRequest.getEmail())
                .name(supplierRequest.getName())
                .phoneNumber(supplierRequest.getPhoneNumber())
                .contactInformation(supplierRequest.getContactInformation())
                .contactPerson(supplierRequest.getContactPerson())
                .paymentTerms(supplierRequest.getPaymentTerms())
                .build();

        return supplierRepository.save(supplier);

    }
    public List<Supplier> getAllSuppliers(){
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + supplierId));
    }

}
