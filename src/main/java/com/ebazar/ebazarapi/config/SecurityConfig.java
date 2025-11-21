package com.ebazar.ebazarapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // We’re building a stateless REST API (no CSRF tokens, no form login)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // 🔓 Swagger & API docs should be public (for now)
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                
                // 🔓 Public auth endpoint (sign up)
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                // 🔓 Public catalogue endpoints (anyone can see products)
                .requestMatchers(HttpMethod.GET,
                        "/api/products",
                        "/api/products/slug/**",
                        "/api/brands",
                        "/api/categories"
                ).permitAll()

                // 🔒 Admin-only product management endpoints
                .requestMatchers("/api/products/**").hasRole("ADMIN")
                
                // 🔒 Admin-only brand management
                .requestMatchers("/api/brands/**").hasRole("ADMIN")

                // 🔒 Admin-only category management
                .requestMatchers("/api/categories/**").hasRole("ADMIN")

                // Everything else must be authenticated
                .anyRequest().authenticated()
            )
            // Use simple HTTP Basic for now (username/password in request)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Simple in-memory users for local dev
    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN") // -> ROLE_ADMIN
                .build();

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER") // -> ROLE_USER
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}