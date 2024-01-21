package com.rose.procurement.newsletter;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.email.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;

    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.emailService = emailService;
    }

    public Subscriber subscribe(String email) throws ProcureException {
        // Check if the subscriber already exists
        Optional<Subscriber> existingSubscriber = subscriberRepository.findByEmail(email);
        if (existingSubscriber.isPresent()) {
            throw  ProcureException.builder().message("subscriber already exists with that email").metadata("subscriber").build();
        }
        String subject = "Subscribing to out Newsletter";
        String text = "Hello, \n\n" + "Thank you for contacting us" +
                "You will get important news about our offers\n\n"
                + "\n\nBest Regards,\nProcureSwift Company";
        emailService.sendEmail(email,subject,text);
        // Create a new subscriber
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email);
        // Save the subscriber to the database
        return subscriberRepository.save(subscriber);
    }
}
