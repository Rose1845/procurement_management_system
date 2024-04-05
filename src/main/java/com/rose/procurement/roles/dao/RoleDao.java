package com.rose.procurement.roles.dao;

import com.rose.procurement.roles.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role,Long> {
    Role findRoleByName(String name);
    @Query("SELECT e FROM  Role e WHERE e.isDefault=?1")
    Role findDefaultRole(Boolean isDefault);
    Boolean existsByIsDefault(Boolean isDefault);
    Boolean existsByName(String name);
}
