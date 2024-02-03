package com.rose.procurement.invoice;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InvoiceMapper invoiceMapper;
    private final PurchaseOrderMapper purchaseOrderMapper;

    public InvoiceService(InvoiceRepository invoiceRepository, PurchaseOrderRepository purchaseOrderRepository,
                          InvoiceMapper invoiceMapper,
                          PurchaseOrderMapper purchaseOrderMapper) {
        this.invoiceRepository = invoiceRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.invoiceMapper = invoiceMapper;
        this.purchaseOrderMapper = purchaseOrderMapper;
    }
    public List<Invoice> getAllInvoices() {
      return new ArrayList<>(invoiceRepository.findAll());
    }
    public Optional<InvoiceDto> getInvoicesByInvoiceId(String invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        return invoice.map(invoiceMapper::toDto);
    }

    public InvoiceDto createInvoice(InvoiceDto invoiceDto) throws ProcureException {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findByPurchaseOrderId(invoiceDto.getPurchaseOrderId());
        if(purchaseOrder.isEmpty()){
            throw ProcureException.builder().metadata("create-invoice").message("purchase order with id do not exists").build();
        }
        Invoice invoiceDto1 = invoiceMapper.toEntity(invoiceDto);
        invoiceDto1.setInvoiceNumber(invoiceDto.getInvoiceNumber());
        invoiceDto1.setDueDate(invoiceDto.getDueDate());
        invoiceDto1.setTotalAmount(invoiceDto.getTotalAmount());
        purchaseOrder.ifPresent(invoiceDto1::setPurchaseOrder);
        Invoice savedInvoice = invoiceRepository.save(invoiceDto1);
        return invoiceMapper.toDto(savedInvoice);
    }

    public List<?> getInvoiceWithDetails(String invoiceId) {
        return invoiceRepository.findInvoiceWithDetailsById(invoiceId);
    }
    public List<Object[]> getInvoiceDetails1ByInvoiceId(String invoiceId) {
        return invoiceRepository.findInvoiceDetails1ByInvoiceId(invoiceId);
    }

}
