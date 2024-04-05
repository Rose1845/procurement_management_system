package com.rose.procurement.roles.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_role_name",columnNames = "name"))
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    private Boolean isDefault;
//    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
//    private Set<User> users=new HashSet<>();

}
