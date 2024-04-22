package com.rose.procurement.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoRequestController {

    private final DemoRequestRepository demoRequestRepository;

    public DemoRequestController(DemoRequestRepository demoRequestRepository) {
        this.demoRequestRepository = demoRequestRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestDemo(@RequestBody DemoRequest demoRequest) {
        // Set request date
        DemoRequest demoRequest1 = new DemoRequest();
        demoRequest1.setEmail(demoRequest.getEmail());
        demoRequest1.setDescription(demoRequest.getDescription());
        demoRequest1.setFirstName(demoRequest.getFirstName());
        demoRequest1.setPhoneNumber(demoRequest.getPhoneNumber());
        demoRequest1.setLastName(demoRequest.getLastName());
        demoRequest1.setCompanyName(demoRequest.getCompanyName());
        // Save demo request to the database
        demoRequestRepository.save(demoRequest1);
        // Return a success response
        return new ResponseEntity<>("Demo request submitted successfully", HttpStatus.OK);
    }

}
