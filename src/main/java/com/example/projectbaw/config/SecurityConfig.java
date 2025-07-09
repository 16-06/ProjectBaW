package com.example.projectbaw.config;

import com.example.projectbaw.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter     jwtAuthFilter;
    private final RateLimitFilter   rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/api/users/public/**",
                                        "/api/vote/public/**"
                                )
                                .permitAll()
                                .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                                .requestMatchers("/api/moderator/**").hasAnyRole(Role.ADMIN.name(),Role.MODERATOR.name())
                                .requestMatchers(
                                        "/api/users/**",
                                        "/api/profile/**",
                                        "/api/vote-comments/**",
                                        "/api/vote/**",
                                        "/api/whoVoted/**",
                                        "/api/vote-options/**"
                                ).hasAnyRole(Role.USER.name(),Role.ADMIN.name(),Role.MODERATOR.name())

                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(rateLimitFilter,   UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter,     UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder BCryptpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


}
