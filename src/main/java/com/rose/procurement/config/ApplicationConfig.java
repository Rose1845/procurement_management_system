package com.rose.procurement.config;


import com.rose.procurement.advice.ProcureException;
import com.rose.procurement.users.dao.UserDao;
import com.rose.procurement.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final UserDao repository;

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      User u = repository.findByUsername(username);
      if (u == null) {
        try {
          throw ProcureException.builder()
                  .message("User account not found")
                  .metadata("auth error")
                  .statusCode(404)
                  .build();
        } catch (ProcureException e) {
          throw new RuntimeException(e);
        }
      }
      return u;
    };
  }
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }
  @Bean
  public AuditorAware<Integer> auditorAware() {
    return  new AuditingConfig();
  }
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
