package com.rose.procurement.contact;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
public class ContactController {
    private final ContactMessageService contactMessageService;

    public ContactController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping("/message")
    public ResponseEntity<String> submitContactMessage(@RequestBody ContactMessage contactMessage) {
        // Save the contact message to the database
        ContactMessage savedMessage = contactMessageService.saveContactMessage(contactMessage);

        // You can add additional logic here, such as sending a confirmation email to the user

        return ResponseEntity.ok("Message submitted successfully");
    }
}
