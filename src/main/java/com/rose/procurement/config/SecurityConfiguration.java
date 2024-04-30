package com.rose.procurement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/api/v1/forgotPassword/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/api/v1/demo/**",
            "api/newsletter/subscribe/**",
            "/api/demo/**",
//            "/api/v1/items",
//            "/api/v1/roles/**",
            "/api/v1/contract/contract-items/**",
            "/api/v1/contract/contract-items/**",
            "/api/v1/contract/send-to-supplier/**",
            "/api/v1/purchase-order/get/order-items/**",
            "/api/v1/purchase-order/approve/**",
            "/api/v2/purchase-order/{purchaseOrderId}/edit2-offer-unit-prices2/**",
            "/api/v1/purchase-order/send-order-to-supplier/**",
            "/api/v1/purchase-request/**",
            "api/send-to-supplier/**",
            "/api/v1/suppliers/supplier/**",
            "/api/contact/message",
            "/swagger-ui.html"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
//                                .requestMatchers("/api/v1/suppliers/**","/api/v1/invoices/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//                                .requestMatchers(GET, "/api/v1/suppliers/**","/api/v1/invoices/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name(),STAFF_READ.name())
//                                .requestMatchers(POST, "/api/v1/invoices/**","/api/v1/items/create","/api/v1/purchase-order/create","/api/v1/purchase-order/create-from-contract/**","/api/v1/purchase-request/create").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name(),STAFF_CREATE.name())
////                                .requestMatchers(PUT, "/api/v1/**/").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name(),STAFF_UPDATE.name())
////                                .requestMatchers(DELETE, "/api/v1/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.setAllowedOrigins(List.of(CorsConfiguration.ALL));
        corsConfiguration.setAllowedMethods(List.of(CorsConfiguration.ALL));
        corsConfiguration.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        source.registerCorsConfiguration("/**", corsConfiguration);
        source.registerCorsConfiguration("/api/v1/**", corsConfiguration);
        return new CorsFilter(source);
    }

}
