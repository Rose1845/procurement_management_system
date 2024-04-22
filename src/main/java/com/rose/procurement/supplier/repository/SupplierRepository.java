package com.rose.procurement.supplier.repository;

import com.rose.procurement.supplier.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {
    Supplier findByName(String name);

    Optional<Supplier> findByVendorId(String vendorId);

    Optional<Supplier> findByVendorId(Supplier supplier);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    boolean existsByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query("delete from Supplier s")
    int deleteFirstBy();

    Page<Supplier> findByNameContaining(String name, Pageable pageable);
}
