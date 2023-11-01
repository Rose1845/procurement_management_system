package com.rose.procurement.invoice;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, PurchaseOrderRepository purchaseOrderRepository) {
        this.invoiceRepository = invoiceRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(invoice -> {
            // Perform any necessary operations here
            // Example: Mapping Invoice to DTO or performing any other transformations

            // return the modified or unmodified invoice
            return invoice;
        }).collect(Collectors.toList());
    }

    public Invoice createInvoice(Invoice invoiceDto){
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findByPurchaseOrderId(invoiceDto.getPurchaseOrder().getPurchaseOrderId());
        if(purchaseOrder.isEmpty()){
            throw new IllegalStateException("purchase order with id do not exists");
        }
        Invoice invoice = Invoice.builder()
//                .invoiceId(invoiceDto.getInvoiceId())
                .invoiceNumber(invoiceDto.getInvoiceNumber())
                .dueDate(invoiceDto.getDueDate())
                .totalAmount(invoiceDto.getTotalAmount())
                .purchaseOrder(purchaseOrder.get())
                .build();
        return invoiceRepository.save(invoice);
    }
}
