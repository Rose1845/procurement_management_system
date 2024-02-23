package com.rose.procurement.supplier.services;
import com.rose.procurement.supplier.entities.Supplier;
import com.rose.procurement.supplier.entities.SupplierReport;
import com.rose.procurement.supplier.repository.SupplierRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final SupplierRepository supplierRepository;

    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {
        List<Supplier> supplierList = supplierRepository.findAll();
        File fIle = ResourceUtils.getFile("suppliers.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(fIle.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(supplierList);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("Created By","ProcureSwift");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
//        JasperExportManager.exportReportToPdfStream(jasperReport,jasperPrint,jasperPrint );

        if(reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,fIle.getAbsolutePath());
        }
        if(reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdf(jasperPrint);

        }
        return "generated";

    }

    public void exportJasperReport(HttpServletResponse response) throws JRException, IOException {
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
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }


}
