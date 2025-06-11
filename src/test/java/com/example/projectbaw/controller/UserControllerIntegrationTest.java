package com.example.projectbaw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.projectbaw.payload.UserDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    private String token;

    private final String username = "testuser12345";
    private final String password = "password123";

    @BeforeAll
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/users";
    }

    @BeforeEach
    void registerAndLoginUser() throws Exception {
        // 1. Rejestracja użytkownika
        UserDto.RequestDto registerRequest = new UserDto.RequestDto();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity(baseUrl, registerRequest, String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 2. Logowanie i pobranie tokena
        UserDto.RequestDto loginRequest = new UserDto.RequestDto();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(baseUrl + "/login", loginRequest, Map.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        this.token = (String) loginResponse.getBody().get("token");
    }

    @Test
    void testRegisterUser_success() {
        String newUsername = "newuser123";
        UserDto.RequestDto request = new UserDto.RequestDto();
        request.setUsername(newUsername);
        request.setPassword("pass123");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Successfully registered");
    }

    @Test
    void testLogin_successful() {
        UserDto.RequestDto request = new UserDto.RequestDto();
        request.setUsername(username);
        request.setPassword(password);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/login", request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("token");
    }

    @Test
    void testGetBasicInfo_authenticated() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDto.ResponseDto> response = restTemplate.exchange(
                baseUrl + "/info/1",
                HttpMethod.GET,
                entity,
                UserDto.ResponseDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUsername()).isEqualTo(username);
    }

    @Test
    void testChangePassword_success() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        UserDto.ChangePassDto changePassDto = new UserDto.ChangePassDto();
        changePassDto.setPassword(password);
        changePassDto.setNewPassword("newpass123");

        HttpEntity<UserDto.ChangePassDto> entity = new HttpEntity<>(changePassDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/changepassword",
                HttpMethod.PUT,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Successful password change");
    }

    @Test
    void testGetAuthenticatedUserInfo_authenticated() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/auth",
                HttpMethod.GET,
                entity,
                Map.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> body = response.getBody();
        assertThat(body).containsKey("id");
        assertThat(body).containsKey("username");
    }
}