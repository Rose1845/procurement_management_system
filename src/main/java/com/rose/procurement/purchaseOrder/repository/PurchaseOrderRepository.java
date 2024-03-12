package com.rose.procurement.purchaseOrder.repository;

import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.supplier.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

    @Query(value = "SELECT " +
            "pos.purchase_order_id AS purchaseOrderId, " +
            "pos.purchase_order_title AS purchaseOrderTitle, " +
            "pos.delivery_date AS deliveryDate, " +
            "pos.terms_and_conditions AS termsAndConditions, " +
            "pos.approval_status AS approvalStatus, " +
            "s.vendor_id AS supplierVendorId, " +
            "s.name AS supplierName, " +
            "s.contact_person AS supplierContactPerson, " +
            "s.contact_information AS supplierContactInformation, " +
            "s.p_o_box AS supplierAaddress_box, " +
            "s.country AS supplierAddress_country, " +
            "s.city AS supplierAddressCity, " +
            "s.location AS supplierAddressLocation, " +
            "s.email AS supplierEmail, " +
            "s.phone_number AS supplierPhoneNumber, " +
            "s.payment_type AS supplierPaymentType, " +
            "s.terms_and_conditions AS supplierTermsAndConditions, " +
            "oi.item_id AS orderItemId, " +
            "oi.purchase_order_id AS oiPurchaseOrderId, " +
            "oi.item_id AS oiItemId, " +
            "i.item_description AS itemItemDescription, " +
            "i.item_name AS itemItemName, " +
            "i.item_id AS itemItemId, " +
            "i.quantity AS itemQuantity, " +
            "i.unit_price AS itemUnitPrice, " +
            "i.total_price AS itemTotalPrice " +
            "FROM purchase_order pos " +
            "JOIN supplier s ON s.vendor_id = pos.supplier_id " +
            "JOIN order_items oi ON oi.purchase_order_id = pos.purchase_order_id " +
            "JOIN item i ON i.item_id = oi.item_id " +
            "WHERE pos.purchase_order_id = :purchaseOrderId",
            nativeQuery = true)
    List<Object[]> findPurchaseOrderDetailsByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);
    Optional<PurchaseOrder> findByPurchaseOrderId(Long purchaseOrderId);

    @Query("SELECT po FROM PurchaseOrder po WHERE MONTH(po.createdAt) = :month")
    List<PurchaseOrder> findPurchaseOrdersByMonth(@Param("month") int month);
    PurchaseOrder findByPurchaseOrderTitle(String purchaseOrderTitle);
    @Query("SELECT p FROM PurchaseOrder p LEFT JOIN FETCH p.items  LEFT JOIN FETCH p.supplier WHERE p.purchaseOrderId = :purchaseOrderId")
    Optional<PurchaseOrder> findByIdWithItems(@Param("purchaseOrderId") Long purchaseOrderId);

    List<PurchaseOrder> findBySupplier(Supplier supplier);

//@Query(value = "SELECT po.purchase_order_title AS po_name, po.created_by AS po_created_by, po.approval_status AS po_status, " +
//        "po.payment_type AS po_payment_terms, po.terms_and_conditions AS po_terms_and_conditions, " +
//        "s.name AS supplier_name, it.* " +
//        "FROM purchase_order po " +
//        "LEFT JOIN supplier s ON po.supplier_id = s.vendor_id " +
//        "LEFT JOIN order_items oi ON po.purchase_order_id = oi.purchase_order_id " +
//        "LEFT JOIN item it ON oi.item_id = it.item_id " +
//        "WHERE po.purchase_order_id = :purchaseOrderId", nativeQuery = true)
//Optional<PurchaseOrder> findByIdWithItems(@Param("purchaseOrderId") Long purchaseOrderId);

//@Query(value = "SELECT po.purchase_order_id, po.purchase_order_title, po.created_by, po.approval_status, " +
//        "po.payment_type, po.terms_and_conditions, " +
//        "i.item_name, i.quantity, i.total_price, i.item_number, i.unit_price, " +
//        "s.name AS supplier_name,\n" +
//        "    s.p_o_box AS supplier_address_box,\n" +
//        "    s.country AS supplier_address_country,\n" +
//        "    s.city AS supplier_address_city,\n" +
//        "    s.location AS supplier_address_location\n" +
//        "FROM purchase_order po\n" +
//        "JOIN order_items oi ON po.purchase_order_id = oi.purchase_order_id\n" +
//        "JOIN item i ON oi.item_id = i.item_id\n" +
//        "JOIN supplier s ON po.supplier_id = s.vendor_id\n" +
//        "WHERE po.purchase_order_id  = :purchaseOrderId" ,nativeQuery = true)
//Optional<PurchaseOrder> findByIdWithItems(@Param("purchaseOrderId") Long purchaseOrderId);
}
