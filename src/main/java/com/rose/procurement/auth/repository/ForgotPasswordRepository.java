package com.rose.procurement.auth.repository;

import com.rose.procurement.auth.entity.ForgotPassword;
import com.rose.procurement.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Long> {
    ForgotPassword findByUserAndExpirationTimeAfter(User user, Date expirationTime);

    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}
