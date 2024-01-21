package com.rose.procurement.newsletter;

import com.rose.procurement.advice.ProcureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/newsletter")
public class NewsletterController {
    private final SubscriberService subscriberService;

    public NewsletterController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToNewsletter(@RequestParam String email) throws ProcureException {
        Subscriber subscriber = subscriberService.subscribe(email);
        return ResponseEntity.ok("Subscribed successfully");
    }
}
