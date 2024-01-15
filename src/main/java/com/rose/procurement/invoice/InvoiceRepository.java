package com.rose.procurement.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String> {
    @Query(
            value = "SELECT i.*, po.*, it.*, s.name AS supplier_name, s.address_box AS supplier_address_box, " +
                    "s.country AS supplier_address_country, s.city AS supplier_address_city, " +
                    "s.location AS supplier_address_location, u.username AS created_by_username, " +
                    "u.email AS created_by_email " +
                    "FROM invoice i " +
                    "JOIN purchase_order po ON i.purchase_order_id = po.purchase_order_id " +
                    "JOIN order_items oi ON po.purchase_order_id = oi.purchase_order_id " +
                    "JOIN item it ON oi.item_id = it.item_id " +
                    "JOIN supplier s ON po.supplier_id = s.vendor_id " +
                    "WHERE i.invoice_id = :invoiceId",
            nativeQuery = true
    )
    List<Invoice> getInvoicesByInvoiceId(String invoiceId);
    @Query(value = "SELECT i.*, po.*, it.*, s.name AS supplier_name, s.p_o_box AS supplier_address_box, " +
            "s.country AS supplier_address_country, s.city AS supplier_address_city, s.location AS supplier_address_location " +
            "FROM invoice i " +
            "JOIN purchase_order po ON i.purchase_order_id = po.purchase_order_id " +
            "JOIN order_items oi ON po.purchase_order_id = oi.purchase_order_id " +
            "JOIN item it ON oi.item_id = it.item_id " +
            "JOIN supplier s ON po.supplier_id = s.vendor_id " +
            "WHERE i.invoice_id = :invoiceId", nativeQuery = true)
    Object findInvoiceDetailsByInvoiceId(@Param("invoiceId") String invoiceId);
}
