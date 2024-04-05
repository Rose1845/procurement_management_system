package com.rose.procurement.invoice;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.enums.InvoiceStatus;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseOrder.repository.PurchaseOrderRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

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

    public void generateAndExportReport(String invoiceId) throws JRException, FileNotFoundException {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        File file = ResourceUtils.getFile("classpath:invoice2.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Your SQL query and other logic for report generation go here

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(invoice.get()));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("purchaseOrderId", invoiceId);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report to a file
        JasperExportManager.exportReportToPdfFile(jasperPrint, "output.pdf");
    }
    public void generateAndExportReport1(String invoiceId) throws JRException, FileNotFoundException {
        // Assuming you have a JDBC URL, username, and password for your database
        String jdbcUrl = "jdbc:mysql://localhost:3306/procure";
        String username = "rose";
        String password = "Atieno18_";

        // Obtain the .jrxml file
        File file = ResourceUtils.getFile("classpath:invoice1.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Parameters to be passed to the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("invoiceId", invoiceId);

        // Establishing a database connection
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Filling the report with data from the database, parameters, and the connection
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Export the report to a file
            JasperExportManager.exportReportToPdfFile(jasperPrint, "output.pdf");
        } catch (Exception e) {
            // Handle exceptions such as SQLExceptions
            e.printStackTrace();
            throw new RuntimeException("Error while generating report", e);
        }
    }
}
