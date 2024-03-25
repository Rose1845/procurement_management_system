package com.rose.procurement.supplier.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequestItemDetail;
import com.rose.procurement.utils.address.Address;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Table(name = "supplier")
@EntityListeners(AuditingEntityListener.class)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "vendor_id")
    private String vendorId;

    private String name;
    @Column(name = "contact_person")

    private String contactPerson;
    @Column(name = "contact_information")
    private String contactInformation;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "box", column = @Column(name = "p.o.box")),
            @AttributeOverride(name = "country",column = @Column(name = "country")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "location", column = @Column(name = "location"))
    })
        @Column(name = "purchase_order_id")

    private Address address;
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Enumerated
    @Column(name ="payment_type")
    private PaymentType paymentType;
    @Column(name = "terms_and_conditions")
    private String termsAndConditions;
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Contract> contracts;
//    @OneToMany(mappedBy = "supplier",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
//    @JsonIgnore
//    private List<Item> items;
    @ManyToMany(mappedBy = "suppliers")
    @JsonIgnore
    private Set<PurchaseRequest> purchaseRequests;
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PurchaseRequestItemDetail> itemDetails;
    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    private List<PurchaseOrder> purchaseOrder;
//    @JsonBackReference
//    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<Offer> offers;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @CreatedBy
    @Column(
            nullable = false,
            updatable = false,
            name = "created_by"
    )
    private Integer createdBy;

}
