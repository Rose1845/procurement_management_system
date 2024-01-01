package com.rose.procurement.contact;

import lombok.Data;

@Data
public class ContactResponse {
    private String name;
    private String title;
    private String email;
    private String message;
}
