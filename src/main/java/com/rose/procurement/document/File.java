package com.rose.procurement.document;

import com.rose.procurement.items.entity.Item;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Arrays;
import java.util.Objects;

@Data
@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String name;
    @Column(name = "content_type")
    private String type;
    @Lob
    @Column(name = "file")
    private byte[] data;
}
