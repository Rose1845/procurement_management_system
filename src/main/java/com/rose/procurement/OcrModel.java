package com.rose.procurement;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;




public class OcrModel {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

    private MultipartFile Image;

    public OcrModel() {
    }

    public OcrModel(MultipartFile Image) {
        this.Image = Image;

    }


    public void setImage(MultipartFile image) {
        Image = image;
    }

}
