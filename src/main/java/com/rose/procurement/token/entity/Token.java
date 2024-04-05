package com.rose.procurement.token.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rose.procurement.token.nums.TokenType;
import com.rose.procurement.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Token {

    @Id
    @GeneratedValue
    public Long id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "fk_user_id")
    public User user;
}