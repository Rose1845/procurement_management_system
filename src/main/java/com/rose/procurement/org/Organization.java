package com.rose.procurement.org;

import com.rose.procurement.utils.address.Address;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String phoneNumber;
    @Embedded
    private Address address;
}