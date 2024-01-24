package com.rose.procurement.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String> {
    @Query(value = "SELECT inv.*, " +
            "po.purchase_order_id as purchaseOrder_purchaseOrderId, " +
            "po.purchase_order_title as purchaseOrder_purchaseOrderTitle, " +
            "po.delivery_date as purchaseOrder_deliveryDate, " +
            "po.terms_and_conditions as purchaseOrder_termsAndConditions, " +
            "s.vendor_id as supplier_vendorId, " +
            "s.name as supplier_supplierName, " +
            "it.item_id as item_itemId, " +
            "it.item_name as item_itemName " +
            "FROM invoice inv " +
            "LEFT JOIN purchase_order po ON inv.purchase_order_id = po.purchase_order_id " +
            "LEFT JOIN supplier s ON po.supplier_id = s.vendor_id " +
            "LEFT JOIN order_items oi ON po.purchase_order_id = oi.purchase_order_id " +
            "LEFT JOIN item it ON oi.item_id = it.item_id " +
            "WHERE inv.invoice_id = :invoiceId", nativeQuery = true)
    Optional<Invoice> findInvoiceWithDetailsById(@Param("invoiceId") String invoiceId);


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
