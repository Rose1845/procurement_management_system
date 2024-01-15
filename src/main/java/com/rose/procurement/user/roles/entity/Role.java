package com.rose.procurement.user.roles.entity;

import com.rose.procurement.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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
