package com.rose.procurement.test;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StudentDto {
    private Integer id;
    private String name;
    private Set<String> courses = new HashSet<>();
}