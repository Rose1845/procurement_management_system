package com.rose.procurement.items.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.category.entity.Category;
import com.rose.procurement.document.File;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String itemId;
    private String itemName;
    private String itemNumber;
    private String itemDescription;
    private int quantity;
    private double unitPrice;
    @ManyToOne
    @JoinColumn(name = "categoryId")
    @JsonBackReference
    private Category category;
}
