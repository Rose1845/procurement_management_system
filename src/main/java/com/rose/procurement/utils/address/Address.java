package com.rose.procurement.utils.address;

import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {
    private  String box;
    private String country;
    private String city;
    private String location;
}
