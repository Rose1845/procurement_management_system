package com.rose.procurement.user.roles.services;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.user.roles.entity.Role;
import com.rose.procurement.user.roles.requests.CreateRoleRequest;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<List<Role>> initializeRoles() throws ProcureException;
    Role createRole(CreateRoleRequest roleRequest) throws ProcureException;
    Role updateRole(Long roleId,CreateRoleRequest roleRequest) throws ProcureException;
   Optional<List<Role>> findRoles();
    Role findRoleByName(String name) throws ProcureException;
    Optional<Role> findRoleById(Long id) throws ProcureException;
    Optional<Role> findDefaultRole(Boolean defaultRole) throws ProcureException;

}
