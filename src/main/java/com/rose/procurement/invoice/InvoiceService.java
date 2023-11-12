package com.rose.procurement.invoice;

import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InvoiceMapper invoiceMapper;

    public InvoiceService(InvoiceRepository invoiceRepository, PurchaseOrderRepository purchaseOrderRepository,
                          InvoiceMapper invoiceMapper
                         ) {
        this.invoiceRepository = invoiceRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.invoiceMapper = invoiceMapper;
    }
    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(invoiceMapper::toDto).collect(Collectors.toList());

    }

    @Transactional
    public InvoiceDto createInvoice(InvoiceDto invoiceDto){
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findByPurchaseOrderId(invoiceDto.getPurchaseOrder().getPurchaseOrderId());
        if(purchaseOrder.isEmpty()){
            throw new IllegalStateException("purchase order with id do not exists");
        }
        Invoice invoiceDto1 = invoiceMapper.toEntity(invoiceDto);
        invoiceDto1.setInvoiceNumber(invoiceDto.getInvoiceNumber());
        invoiceDto1.setDueDate(invoiceDto.getDueDate());
        invoiceDto1.setTotalAmount(invoiceDto.getTotalAmount());
        invoiceDto1.setPurchaseOrder(purchaseOrder.get());
//        Invoice invoice = Invoice.builder()
//                .invoiceNumber(invoiceDto.getInvoiceNumber())
//                .dueDate(invoiceDto.getDueDate())
//                .totalAmount(invoiceDto.getTotalAmount())
//                .purchaseOrder(purchaseOrder.get())
//                .build();
        Invoice savedInvoice = invoiceRepository.save(invoiceDto1);
        return invoiceMapper.toDto(savedInvoice);
    }
}
