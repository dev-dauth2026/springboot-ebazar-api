package com.ebazar.ebazarapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Swagger public
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                // Auth: registration is public, /me requires login
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()

                // Public catalogue endpoints
                .requestMatchers(HttpMethod.GET,
                        "/api/products",
                        "/api/products/slug/**",
                        "/api/brands",
                        "/api/categories"
                ).permitAll()

                // Admin-only product management
                .requestMatchers("/api/products/**").hasRole("ADMIN")

                // Admin-only brand management
                .requestMatchers("/api/brands/**").hasRole("ADMIN")

                // Admin-only category management
                .requestMatchers("/api/categories/**").hasRole("ADMIN")

                // Everything else must be authenticated
                .anyRequest().authenticated()
            )
            // Simple HTTP Basic using DB users (CustomUserDetailsService)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}