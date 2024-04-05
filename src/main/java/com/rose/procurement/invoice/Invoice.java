package com.rose.procurement.invoice;

import com.rose.procurement.enums.InvoiceStatus;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String invoiceId;
    private String invoiceNumber;
    private LocalDate dueDate;
    private LocalDate invoiceDate;
    @Enumerated
    private InvoiceStatus invoiceStatus;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_id")
//    @JsonIgnore
    private PurchaseOrder purchaseOrder;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @CreatedBy
    @Column(name = "created_by")
    private Integer createdBy;

    private String generateInvoiceNumber() {
        // Generate 3 random letters
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomLetters = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            randomLetters.append(letters.charAt(random.nextInt(letters.length())));
        }
        // Generate 2 random numbers
        int randomNumber = random.nextInt(90) + 10;
        // Combine "QR", letters, and numbers
        return "IN" + randomLetters.toString() + randomNumber;
    }

    @PrePersist
    private void prePersist() {
        // Set invoiceNumber before persisting the entity
        if (invoiceNumber == null || invoiceNumber.isEmpty()) {
            invoiceNumber = generateInvoiceNumber();
        }
    }
}
