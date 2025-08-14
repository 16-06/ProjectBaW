package com.example.projectbaw.integrationTest;

import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginTest {


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturn401WhenUserNotAuthenticated() {

        UserDto.LoginDto dto = new UserDto.LoginDto();
        dto.setUsername("inactiveUser");
        dto.setPassword("password");

        when(userService.login(any())).thenReturn(Optional.empty());
        when(userService.isAccountEnabled("inactiveUser")).thenReturn(false);

        webTestClient.post()
                .uri("/api/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(String.class)
                .consumeWith(response -> {

                    String expected = "Account not activated, check your email for activation link";
                    String actual = response.getResponseBody();
                    assertNotNull(actual, "Response body is null");
                    assertEquals(expected, actual);
                    });
    }

    @Test
    void shouldReturn403WhenAccountLocked() {

        UserDto.LoginDto dto = new UserDto.LoginDto();
        dto.setUsername("bannedUser");
        dto.setPassword("password");

        when(userService.login(any())).thenReturn(Optional.empty());
        when(userService.isAccountEnabled("bannedUser")).thenReturn(true);
        when(userService.isAccountBanned("bannedUser")).thenReturn(true);

        webTestClient.post()
                .uri("/api/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String expected = "Account is banned, check your email for more information";
                    String actual = response.getResponseBody();
                    assertNotNull(actual, "Response body is null");
                    assertEquals(expected, actual);

                });

    }

    @Test
    void shouldReturn202When2FaEnable() {
        UserDto.LoginDto dto = new UserDto.LoginDto();
        dto.setUsername("2faUser");
        dto.setPassword("password");

        when(userService.login(any())).thenReturn(Optional.empty());
        when(userService.isAccountEnabled("2faUser")).thenReturn(true);
        when(userService.isAccountBanned("2faUser")).thenReturn(false);
        when(userService.isTwoFactorEnabled("2faUser")).thenReturn(true);

        webTestClient.post()
                .uri("/api/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String expected = "Two-factor authentication required, please provide the code sent to your email";
                    String actual = response.getResponseBody();
                    assertNotNull(actual, "Response body is null");
                    assertEquals(expected, actual);

                });
    }

    @Test
    void shouldReturn200WithToken() {
        UserDto.LoginDto dto = new UserDto.LoginDto();
        dto.setUsername("validUser");
        dto.setPassword("validPassword");

        String randomToken = "mocked.jwt.token";

        when(userService.login(any())).thenReturn(Optional.of(randomToken));


        webTestClient.post()
                .uri("/api/users/public/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String actual = response.getResponseBody();
                    assertNotNull(actual, "Response body is null");
                    assertEquals(randomToken, actual);

                });
    }

}
