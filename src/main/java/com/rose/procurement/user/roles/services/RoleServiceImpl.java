package com.rose.procurement.user.roles.services;

import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.user.roles.dao.RoleDao;
import com.rose.procurement.user.roles.entity.Role;
import com.rose.procurement.user.roles.requests.CreateRoleRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    @Override
    public Optional<List<Role>> initializeRoles() throws ProcureException {

            List<Role> roles = roleDao.findAll();
            if (roles.toArray().length>0) {
                throw ProcureException.builder().message("Roles already initialized").metadata("recurse/init").statusCode(500).build();
            }
            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("Admin role")
                    .isDefault(Boolean.TRUE)
                    .build();
            Role approveRole = Role.builder()
                    .isDefault(Boolean.TRUE)
                    .name("APPROVER")
                    .description("Approve role")
                    .build();
        Role requisitionRole = Role.builder()
                .isDefault(Boolean.TRUE)
                .name("REQUISITIONER")
                .description("REQUEST role")
                .build();
            return Optional.of(roleDao.saveAll(List.of(adminRole, approveRole,requisitionRole)));

    }

    @Override
    public Role createRole(CreateRoleRequest roleRequest) throws ProcureException {
       if (roleDao.existsByName(roleRequest.getName())){
           throw ProcureException.builder()
                   .message("Role already exist")
                   .metadata("trying to add existing record")
                   .build();
       }
        if (roleDao.existsByIsDefault(roleRequest.getIsDefault())){
            throw ProcureException.builder()
                    .message("Default role already exist")
                    .metadata("trying to add existing record")
                    .build();
        }
        Role newRole = Role.builder()
                .name(roleRequest.getName())
                .isDefault(roleRequest.getIsDefault())
                .description(roleRequest.getDescription())
                .build();
        return roleDao.save(newRole);
    }

    @Override
    public Role updateRole(Long roleId, CreateRoleRequest roleRequest) throws ProcureException {
        if (!roleDao.existsById(roleId)){
            throw ProcureException.builder()
                    .message("Role does not exist")
                    .metadata("trying to add existing record")
                    .build();
        }
        if (roleDao.existsByName(roleRequest.getName())){
            throw ProcureException.builder()
                    .message("Role already exist")
                    .metadata("trying to add existing record")
                    .build();
        }
        if (roleDao.existsByIsDefault(roleRequest.getIsDefault())){
            throw ProcureException.builder()
                    .message("Default role already exist")
                    .metadata("trying to add existing record")
                    .build();
        }
        Optional<Role> role = roleDao.findById(roleId);
        role.get().setDescription(roleRequest.getDescription());
        role.get().setIsDefault(roleRequest.getIsDefault());
        role.get().setName(roleRequest.getName());
        return  roleDao.save(role.get());
    }

    @Override
    public Optional<List<Role>> findRoles() {
       List<Role> roles;
        roles = roleDao.findAll();
        if (roles.isEmpty()) {
            throw new RuntimeException("No roles found");
        }
        return Optional.of(roles);
    }

    @Override
    public Role findRoleByName(String name) throws ProcureException {
        if (!roleDao.existsByName(name)) {
            throw ProcureException.builder().message("Role with " + name + " does not exist").build();
        }
        return roleDao.findRoleByName(name);
    }

    @Override
    public Optional<Role> findRoleById(Long id) throws ProcureException {
        if (!roleDao.existsById(id)){
                throw ProcureException.builder().message("Role with " + id.toString() + " does not exist").build();
        }
        return roleDao.findById(id);
    }

    @Override
    public Optional<Role> findDefaultRole(Boolean isDefault) throws ProcureException {
        if (!roleDao.existsByIsDefault(isDefault)) {
            throw ProcureException.builder().message("Default role not found").build();
        }
        Role role = roleDao.findDefaultRole(isDefault);

        return Optional.of(role);
    }
}
