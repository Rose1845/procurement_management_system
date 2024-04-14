package com.rose.procurement.users.dao;

import com.rose.procurement.users.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long>{
    User findByEmailOrUsername(String email, String username);
    Boolean existsByEmailOrUsername(String email,String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    User findByEmail(String email);
    User findByUsername(String username);
   @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = :roleId")
   List<User> findByRoleId(Long roleId);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);
}
