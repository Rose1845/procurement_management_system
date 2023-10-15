package com.rose.procurement.items.entity;

import com.rose.procurement.document.File;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Item {
    @Id
    private Long id;
    private String itemName;
    private String itemNumber;
    private String itemDescription;
    private int quantity;
    private double unitPrice;
    @OneToOne
    @JoinColumn(name = "file_id")
    private File itemImage;
}
