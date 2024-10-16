package com.rose.procurement.document;

import jakarta.persistence.*;
import lombok.Data;

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
