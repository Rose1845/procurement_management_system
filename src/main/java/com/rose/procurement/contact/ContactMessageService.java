package com.rose.procurement.contact;

import com.rose.procurement.email.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;
    private final EmailService emailService;

    public ContactMessageService(ContactMessageRepository contactMessageRepository, EmailService emailService) {
        this.contactMessageRepository = contactMessageRepository;
        this.emailService = emailService;
    }

    public ContactMessage saveContactMessage(ContactMessage contactMessage) {
        String subject = "Contact Us";
        String text = "Hello \n\n" + contactMessage.getName()+ "," + "Thank you for contacting us" +
                "We will get back to you soon.\n\n"
                + "Your Message " + contactMessage.getMessage() + "\n"
                + "\n\nBest Regards,\nProcureSwift Company";
        emailService.sendEmail(contactMessage.getEmail(),subject,text);
        return contactMessageRepository.save(contactMessage);
    }
}

