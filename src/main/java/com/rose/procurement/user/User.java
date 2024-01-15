package com.rose.procurement.user;

import com.rose.procurement.user.token.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;
//  @ManyToMany(fetch = FetchType.EAGER,cascade ={CascadeType.MERGE,CascadeType.REFRESH})
//  @JoinTable(name = "user_with_roles",
//          joinColumns = {@JoinColumn(name = "user_side_id")},
//          inverseJoinColumns = {@JoinColumn(name = "role_side_id")})
//  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
//  private Set<Role> roles=new HashSet<>();

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
