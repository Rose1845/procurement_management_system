package com.rose.procurement.document;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

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
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            byte[] fileContent = file.getBytes();
            File savefile = new File(fileName,contentType,fileContent);
            fileRepository.save(savefile);
            return "File saved successfully";
        }

        catch(Exception e) {
            return "File not saved";
        }
    }
    @GetMapping()
    public Optional<File> getFile(@PathVariable String id ){
       return fileRepository.findById(id);
    }
}
