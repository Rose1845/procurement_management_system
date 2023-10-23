//package com.rose.procurement;
//
//import com.rose.procurement.document.File;
//import net.sourceforge.tess4j.ITesseract;
//import net.sourceforge.tess4j.Tesseract;
//import org.springframework.stereotype.Service;
//
//@Service
//public class OCRService {
//    public String performOCR(File imageFile) {
//        ITesseract tesseract = new Tesseract();
//        try {
//            tesseract.setDatapath("tessdata"); // path to the 'tessdata' folder containing language data files
//            tesseract.setLanguage("eng"); // specify the language used in the image
//            return tesseract.doOCR(imageFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error during OCR: " + e.getMessage();
//        }
//    }
//}
