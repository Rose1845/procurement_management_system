package com.rose.procurement.supplier.services;

import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final SupplierRepository supplierRepository;


    public ResponseEntity<Resource> exportReport() throws IOException, JRException {
        List<Supplier> supplierList = supplierRepository.findAll();
        JasperPrint jasperPrint = generateSupplierReport(supplierList);

        // Export to byte array
        byte[] reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Prepare file for download
        ByteArrayResource resource = new ByteArrayResource(reportBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=suppliers.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(reportBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    private JasperPrint generateSupplierReport(List<Supplier> supplierList) throws JRException, FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:suppliers.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(supplierList);
        Map<String, Object> parameters = new HashMap<>();
        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    public String exportJasperReport() throws JRException, IOException {
        List<Supplier> suppliers = supplierRepository.findAll();
        //Get file and compile it
        File file = ResourceUtils.getFile("classpath:suppliers.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(suppliers);
        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("", "");
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //Export report
        JasperExportManager.exportReportToPdfFile(jasperPrint, "output.pdf");
        return "generate";
    }
}
