package com.example.projectbaw.controller;

import com.example.projectbaw.config.JwtUtil;
import com.example.projectbaw.mapper.UserMapper;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
/*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, jwtUtil,userMapper)).build();
    }

    // Test dla POST /api/users
    @Test
    void registerUser_shouldReturnOk() throws Exception {
        String jsonRequest = """
                {
                    "username": "testuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully registered"));

        verify(userService, times(1)).registerUser(any(UserDto.RegisterDto.class));
    }


    @Test
    void login_invalidCredentials() throws Exception {
        String jsonRequest = """
                {
                    "username": "wrong",
                    "password": "wrongpass"
                }
                """;

        when(userService.login("wrong", "wrongpass")).thenReturn(java.util.Optional.empty());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid username or password"));
    }


    @Test
    void getBasicInfo_notFound() throws Exception {
        when(userService.getById(999L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/users/info/999"))
                .andExpect(status().isNotFound());
    }

    // Test dla PUT /api/users/changepassword
    @Test
    void changePassword_success() throws Exception {
        String jsonRequest = """
                {
                    "password": "oldPass",
                    "newPassword": "newPass"
                }
                """;

        when(userService.changePassword("oldPass", "newPass")).thenReturn(true);

        mockMvc.perform(put("/api/users/changepassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Successful password change"));
    }

    @Test
    void changePassword_failure() throws Exception {
        String jsonRequest = """
                {
                    "password": "wrongOldPass",
                    "newPassword": "newPass"
                }
                """;

        when(userService.changePassword("wrongOldPass", "newPass")).thenReturn(false);

        mockMvc.perform(put("/api/users/changepassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect old password or new password "));
    }

    // Test dla GET /api/users/auth
    @Test
    void getAuthenticatedUser_authenticated() throws Exception {
        when(httpServletRequest.getAttribute("username")).thenReturn("testuser");
        when(httpServletRequest.getAttribute("UserId")).thenReturn(1L);

        mockMvc.perform(get("/api/users/auth")
                        .requestAttr("username", "testuser")
                        .requestAttr("UserId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void getAuthenticatedUser_unauthorized_missingUsername() throws Exception {
        when(httpServletRequest.getAttribute("username")).thenReturn(null);
        when(httpServletRequest.getAttribute("UserId")).thenReturn(1L);

        mockMvc.perform(get("/api/users/auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized1"));
    }

    @Test
    void getAuthenticatedUser_unauthorized_missingUserId() throws Exception {
        when(httpServletRequest.getAttribute("username")).thenReturn("testuser");
        when(httpServletRequest.getAttribute("UserId")).thenReturn(null);

        mockMvc.perform(get("/api/users/auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized1"));
    }*/
}