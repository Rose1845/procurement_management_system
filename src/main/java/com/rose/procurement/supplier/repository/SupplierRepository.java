package com.rose.procurement.supplier.repository;

import com.rose.procurement.supplier.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,String> {
    Supplier findByName(String name);

    Optional<Supplier> findByVendorId(String vendorId);

    Optional<Supplier> findByVendorId(Supplier supplier);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    boolean existsByPhoneNumber(String phoneNumber);
}
