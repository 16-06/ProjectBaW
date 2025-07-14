package com.example.projectbaw.integrationTest;

import com.example.projectbaw.ProjectBaWApplication;
import com.example.projectbaw.config.CustomUserDetails;
import com.example.projectbaw.enums.Role;
import com.example.projectbaw.model.User;
import com.example.projectbaw.model.UserSecurity;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.UserSecurityRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;



@Slf4j
@SpringBootTest(classes = ProjectBaWApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class BaseTestUser {


    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserSecurityRepository securityRepository;

    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;

    @Autowired
    protected ObjectMapper objectMapper;

    protected User seedUser(String username, String email, String rawPassword, boolean enabled, Role role) {

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabledAccount(enabled);
        user.setRole(role);

        UserSecurity us = new UserSecurity();
        us.setUser(user);
        user.setSecurityData(us);

        userRepository.save(user);
        return user;
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        User user = seedUser("john", "john@test.pl", "password123", true, Role.USER);

        CustomUserDetails cud = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                cud,
                null,
                cud.getAuthorities()
        );

        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .setAuthentication(auth);

        log.info(">>> Test user 'john' authenticated for test");
    }


    @AfterEach
    void tearDown() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }


    protected String json(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    protected static final MediaType JSON = MediaType.APPLICATION_JSON;
}
