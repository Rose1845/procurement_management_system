package com.rose.procurement.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String> {
    @Query(value = "SELECT\n" +
            "    i.*,\n" +
            "    po.*,\n" +
            "    it.*,\n" +
            "    s.name AS supplier_name,\n" +
            "    s.p_o_box AS supplier_address_box,\n" +
            "    s.country AS supplier_address_country,\n" +
            "    s.city AS supplier_address_city,\n" +
            "    s.location AS supplier_address_location\n" +
            "FROM\n" +
            "    invoice i\n" +
            "JOIN\n" +
            "    purchase_order po ON i.purchase_order_id = po.purchase_order_id\n" +
            "JOIN\n" +
            "    order_items oi ON po.purchase_order_id = oi.purchase_order_id\n" +
            "JOIN\n" +
            "    item it ON oi.item_id = it.item_id\n" +
            "JOIN\n" +
            "    supplier s ON po.supplier_id = s.vendor_id\n" +
            "\n" +
            "WHERE\n" +
            "    i.invoice_id = :invoiceId", nativeQuery = true)
   List<Object>  findInvoiceDetailsByInvoiceId(@Param("invoiceId") String invoiceId);

    @Query(value = "SELECT\n" +
            "    i.*,\n" +
            "    po.*,\n" +
            "    it.*,\n" +
            "    s.name AS supplier_name,\n" +
            "    s.p_o_box AS supplier_address_box,\n" +
            "    s.country AS supplier_address_country,\n" +
            "    s.city AS supplier_address_city,\n" +
            "    s.location AS supplier_address_location\n" +
            "FROM\n" +
            "    invoice i\n" +
            "JOIN\n" +
            "    purchase_order po ON i.purchase_order_id = po.purchase_order_id\n" +
            "JOIN\n" +
            "    order_items oi ON po.purchase_order_id = oi.purchase_order_id\n" +
            "JOIN\n" +
            "    item it ON oi.item_id = it.item_id\n" +
            "JOIN\n" +
            "    supplier s ON po.supplier_id = s.vendor_id\n" +
            "\n" +
            "WHERE\n" +
            "    i.invoice_id = :invoiceId", nativeQuery = true)
    List<Object[]> findInvoiceDetails1ByInvoiceId(@Param("invoiceId") String invoiceId);

}
