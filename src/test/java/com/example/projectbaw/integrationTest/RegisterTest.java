package com.example.projectbaw.integrationTest;

import com.example.projectbaw.enums.Role;
import com.example.projectbaw.model.User;
import com.example.projectbaw.service.EmailConfirmationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegisterTest extends BaseTestUser {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailConfirmationService emailConfirmationService;

    @Test
    void shouldRegisterNewUser_andSendActivationEmail() throws Exception {
        String body = """
            {
              "username": "john123",
              "email": "john@site.pl",
              "password": "secret123"
            }
            """;



        mockMvc.perform(post("/api/users/public/register")
                        .contentType(JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully registered"));

        User saved = userRepository.findByUsername("john123").orElseThrow();
        assertFalse(saved.isEnabledAccount());

        verify(emailConfirmationService).sendConfirmationEmail(eq("john@site.pl"), anyString());
    }

    @Test
    void shouldRejectDuplicateUsername() throws Exception {
        seedUser("john", "john@site.pl", "secret123", true, Role.USER);

        String body = """
            {"username":"john","email":"dup@site.pl","password":"secret123"}
            """;

        mockMvc.perform(post("/api/users/public/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}

