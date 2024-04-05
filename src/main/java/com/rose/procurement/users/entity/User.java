package com.rose.procurement.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rose.procurement.org.Organization;
import com.rose.procurement.roles.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;
//    @OneToOne(mappedBy = "user")
//    private Organization organization;
    @Column(unique = true, nullable = false)
    private String email;
    private String username;
    private String phoneNumber;
    @ToString.Exclude
    @JsonIgnore
    private String password;
    @Column(unique = true, nullable = false)
    private String firstname;
    private String lastname;
    private String avatar="";
    @ManyToMany(fetch = FetchType.EAGER,cascade ={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name = "user_with_roles",
            joinColumns = {@JoinColumn(name = "user_side_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_side_id")})
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Set<Role> roles=new HashSet<>();


     /**
     * Disabled this to avoid stack overflow error on recursive call of null user with token
     */
//     @JsonManagedReference
//     @OneToMany(mappedBy = "user")
//     private List<Token> tokens;

    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName().toUpperCase())).collect(Collectors
                .toList() );
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}