package com.rose.procurement.config;

import com.rose.procurement.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

//@Configuration
public class AuditingConfig implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
//    @Override
//    public Optional<String> getCurrentAuditor() {
//        Authentication authentication =
//                SecurityContextHolder
//                        .getContext()
//                        .getAuthentication();
//        if (authentication == null ||
//                !authentication.isAuthenticated() ||
//                authentication instanceof AnonymousAuthenticationToken
//        ) {
//            return Optional.empty();
//        }
////        String username = "unknown"; // Set a default value if no user is authenticated
//
//        User userPrincipal = (User) authentication.getPrincipal();
////        if(userPrincipal != null){
////            username = ((UserDetails) userPrincipal).getUsername();
////        }
////        assert userPrincipal != null;
//        return Optional.ofNullable(userPrincipal.getUsername());
//    }
}


