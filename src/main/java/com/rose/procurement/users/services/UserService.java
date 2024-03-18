package com.rose.procurement.users.services;
import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.users.entity.User;
import com.rose.procurement.users.requests.CreateUserRequest;
import com.rose.procurement.users.requests.CreateUserWithRolesRequest;
import com.rose.procurement.users.requests.UpdateUserPasswordRequest;
import com.rose.procurement.users.requests.UpdateUserWithoutPasswordRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    User createUser(CreateUserRequest userRequest);

    User creatUserWithRole(CreateUserRequest user, Long roleId) throws ProcureException;

    List<User> findUsers() throws ProcureException;

    User findUserByEmail(String email) throws ProcureException;

    User findUserByUsername(String email) throws ProcureException;
    List<User> findUsersByRole(Long role) throws ProcureException;
    Optional<User> findUserById(Long id) throws ProcureException;

    User addRolesToUser(Long userId, Set<Long> roleId) throws ProcureException;
    User removeRolesFromUser(Long userId,Set<Long> roleIds) throws ProcureException;
//    List<User> findUsersByRole(Long roleId);
    User createUserWithRoles(CreateUserWithRolesRequest request) throws ProcureException;
    User updateUserWithoutPassword(Long userId, UpdateUserWithoutPasswordRequest request) throws ProcureException;
    User updateUserPassword(Long userId, UpdateUserPasswordRequest request) throws ProcureException;

}
