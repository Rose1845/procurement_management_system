package com.rose.procurement.invoice;

import com.rose.procurement.enums.ApprovalStatus;
import com.rose.procurement.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String> {
//    @Query(value = "SELECT " +
//            "inv.*, " +
//            "po.purchase_order_id as purchaseOrder_purchaseOrderId " +
//            "FROM invoice inv " +
//            "LEFT JOIN purchase_order po ON inv.purchase_order_id = po.purchase_order_id " +
//            "WHERE inv.invoice_id = :invoiceId;" +
//            "SELECT " +
//            "po.*, " +
//            "s.vendor_id as supplier_vendorId, " +
//            "s.name as supplier_supplierName " +
//            "FROM purchase_order po " +
//            "LEFT JOIN supplier s ON po.supplier_id = s.vendor_id " +
//            "WHERE po.purchase_order_id = (SELECT purchase_order_id FROM invoice WHERE invoice_id = :invoiceId);" +
//            "SELECT " +
//            "it.* " +
//            "FROM item it " +
//            "LEFT JOIN order_items oi ON it.item_id = oi.item_id " +
//            "WHERE oi.purchase_order_id = (SELECT purchase_order_id FROM invoice WHERE invoice_id = :invoiceId);", nativeQuery = true)
//    Optional<Invoice> findInvoiceWithDetailsById(@Param("invoiceId") String invoiceId);

//    @Query("SELECT p FROM PurchaseOrder p LEFT JOIN FETCH p.items LEFT JOIN FETCH p.supplier WHERE p.purchaseOrderId = :purchaseOrderId")

//    @Query("SELECT i, po " +
//            "FROM Invoice i " +
//            "LEFT JOIN i.purchaseOrder po  " +
//            "LEFT JOIN po.items it " +
//            "LEFT JOIN po.supplier s " +
//            "WHERE i.invoiceId = :invoiceId")
@Query("SELECT i, po, s, it FROM Invoice i " +
        "LEFT JOIN i.purchaseOrder po " +
        "LEFT JOIN po.supplier s " +
        "LEFT JOIN po.items it " +
        "WHERE i.invoiceId = :invoiceId")
List<?> findInvoiceWithDetailsById(@Param("invoiceId") String invoiceId);


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

    Page<Invoice> findByInvoiceNumberContainingAndCreatedAtBetween(String searchField, LocalDateTime localDateTime, LocalDateTime localDateTime1, Pageable pageable);

    Page<Invoice> findByInvoiceNumberContaining(String searchField, Pageable pageable);

    Page<Invoice> findByCreatedAtBetween(LocalDateTime localDateTime, LocalDateTime localDateTime1, Pageable pageable);

    Page<Invoice> findByPurchaseOrderApprovalStatusAndInvoiceStatusAndPurchaseOrderSupplierVendorId(ApprovalStatus approvalStatus, InvoiceStatus invoiceStatus, String supplierId, Pageable pageable);

    Page<Invoice> findByPurchaseOrderApprovalStatusAndInvoiceStatus(ApprovalStatus approvalStatus, InvoiceStatus invoiceStatus, Pageable pageable);

    Page<Invoice> findByPurchaseOrderApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);

    Page<Invoice> findByInvoiceStatusAndPurchaseOrder_SupplierVendorId(InvoiceStatus invoiceStatus, String supplierId, Pageable pageable);

    Page<Invoice> findByInvoiceStatus(InvoiceStatus invoiceStatus, Pageable pageable);

    Page<Invoice> findByPurchaseOrderSupplierVendorId(String supplierId, Pageable pageable);

    Page<Invoice> findByPurchaseOrderApprovalStatusAndPurchaseOrderSupplierVendorId(ApprovalStatus approvalStatus, String supplierId, Pageable pageable);
}
