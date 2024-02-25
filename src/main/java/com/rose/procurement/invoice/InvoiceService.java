package com.rose.procurement.invoice;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.enums.InvoiceStatus;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.mappers.PurchaseOrderMapper;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
    public List<Invoice> getAllInvoices() {
      return new ArrayList<>(invoiceRepository.findAll());
    }

    public InvoiceDto createInvoice(InvoiceDto invoiceDto) throws ProcureException {
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findByPurchaseOrderId(invoiceDto.getPurchaseOrderId());
        if(purchaseOrder.isEmpty()){
            throw ProcureException.builder().metadata("create-invoice").message("purchase order with id do not exists").build();
        }
        Invoice invoiceDto1 = invoiceMapper.toEntity(invoiceDto);
        invoiceDto1.setInvoiceNumber(generateInvoiceNumber());
        invoiceDto1.setDueDate(invoiceDto.getDueDate());
        invoiceDto1.setInvoiceDate(invoiceDto.getInvoiceDate());
        invoiceDto1.setInvoiceStatus(InvoiceStatus.APPROVED_FOR_PAYMENT);
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
    public Optional<Invoice> getInvoice(String invoiceId){
        return invoiceRepository.findById(invoiceId);
    }

    private String generateInvoiceNumber() {
        // Generate 3 random letters
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomLetters = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            randomLetters.append(letters.charAt(random.nextInt(letters.length())));
        }

        // Generate 2 random numbers
        int randomNumber = random.nextInt(90) + 10;

        // Combine "QR", letters, and numbers
        return "IN" + randomLetters.toString() + randomNumber;
    }

}
