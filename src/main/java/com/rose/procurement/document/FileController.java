package com.rose.procurement.document;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class FileController {
   private final FileRepository  fileRepository;

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping(value = "/saveFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveFile(@RequestParam("file") MultipartFile file){
        try {
            File file1 = new File();
            file1.setName(file.getOriginalFilename());
            file1.setType(file.getContentType());
            file1.setData(file.getBytes());
            fileRepository.save(file1);
            return "File saved successfully";
        }

        catch(Exception e) {
            return "File not saved";
        }
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id ){
       File file = fileRepository.findById(id).orElse(null);
       if(file != null){
           HttpHeaders httpHeaders = new HttpHeaders();
           httpHeaders.setContentType(MediaType.parseMediaType(file.getType()));
           httpHeaders.setContentDisposition(ContentDisposition.attachment().filename(file.getName()).build());
           ByteArrayResource byteArrayResource = new ByteArrayResource(file.getData());
           return ResponseEntity.ok().headers(httpHeaders).body(byteArrayResource);
       }else{
           return ResponseEntity.notFound().build();
        }
    }
}
