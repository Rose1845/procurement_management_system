package com.rose.procurement.supplier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.contract.entities.Contract;
import com.rose.procurement.enums.PaymentType;
import com.rose.procurement.items.entity.Item;
import com.rose.procurement.purchaseOrder.entities.PurchaseOrder;
import com.rose.procurement.purchaseRequest.entities.PurchaseRequest;
import com.rose.procurement.utils.address.Address;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
//@EntityListeners(AuditingEntityListener.class)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vendorId;
    private String name;
    private String contactPerson;
    private String contactInformation;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "box", column = @Column(name = "p.o.box")),
            @AttributeOverride(name = "country",column = @Column(name = "country")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "location", column = @Column(name = "location"))
    })
    private Address address;
    @Email
    private String email;
    private String phoneNumber;
    @Enumerated
    private PaymentType paymentType;
    private String termsAndConditions;
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contract> contracts;
    @OneToMany(mappedBy = "supplier",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Item> items;
    @OneToOne(fetch = FetchType.LAZY,orphanRemoval = true)
    @JsonIgnore
    private PurchaseRequest purchaseRequest;
    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    private List<PurchaseOrder> purchaseOrder;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
//    @CreatedBy
//    @Column(
//            nullable = false,
//            updatable = false
//    )
//    private Integer createdBy;

}
