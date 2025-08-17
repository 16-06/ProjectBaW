package com.example.projectbaw.integrationTest;


import com.example.projectbaw.enums.Role;
import com.example.projectbaw.payload.UserDto;
import com.example.projectbaw.service.ModeratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ModeratorSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ModeratorService moderatorService;

    @Test
    void shouldAllowModeratorToGetUserList() throws Exception{

        UserDto.FullUserDto user1 = new UserDto.FullUserDto();
        user1.setUsername("user1");
        user1.setId(1L);
        user1.setEmail("user1@user.com");
        user1.setBannedAccount(false);
        user1.setTwoFactorEnabled(false);
        user1.setEnabledAccount(true);
        user1.setRole(Role.USER);

        UserDto.FullUserDto user2 = new UserDto.FullUserDto();
        user2.setUsername("user2");
        user2.setId(2L);
        user2.setEmail("user2@user.com");
        user2.setBannedAccount(false);
        user2.setTwoFactorEnabled(false);
        user2.setEnabledAccount(true);
        user2.setRole(Role.USER);

        List<UserDto.FullUserDto> mockUsers = List.of(user1,user2);

        when(moderatorService.getAllUsers()).thenReturn(mockUsers);


        mockMvc.perform(get("/api/moderator/users")
                .with(user("ModUser").roles("MODERATOR"))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                // --- user1 ---
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@user.com"))
                .andExpect(jsonPath("$[0].bannedAccount").value(false))
                .andExpect(jsonPath("$[0].twoFactorEnabled").value(false))
                .andExpect(jsonPath("$[0].enabledAccount").value(true))
                .andExpect(jsonPath("$[0].role").value("USER"))

                // --- user2 ---
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].email").value("user2@user.com"))
                .andExpect(jsonPath("$[1].bannedAccount").value(false))
                .andExpect(jsonPath("$[1].twoFactorEnabled").value(false))
                .andExpect(jsonPath("$[1].enabledAccount").value(true))
                .andExpect(jsonPath("$[1].role").value("USER"));



        verify(moderatorService).getAllUsers();
    }

    @Test
    void shouldAllowAdminToGetUserList() throws Exception{

        UserDto.FullUserDto user1 = new UserDto.FullUserDto();
        user1.setUsername("user1");
        user1.setId(1L);
        user1.setEmail("user1@user.com");
        user1.setBannedAccount(false);
        user1.setTwoFactorEnabled(false);
        user1.setEnabledAccount(true);
        user1.setRole(Role.USER);

        UserDto.FullUserDto user2 = new UserDto.FullUserDto();
        user2.setUsername("user2");
        user2.setId(2L);
        user2.setEmail("user2@user.com");
        user2.setBannedAccount(false);
        user2.setTwoFactorEnabled(false);
        user2.setEnabledAccount(true);
        user2.setRole(Role.USER);

        List<UserDto.FullUserDto> mockUsers = List.of(user1,user2);

        when(moderatorService.getAllUsers()).thenReturn(mockUsers);


        mockMvc.perform(get("/api/moderator/users")
                        .with(user("ModUser").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                // --- user1 ---
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@user.com"))
                .andExpect(jsonPath("$[0].bannedAccount").value(false))
                .andExpect(jsonPath("$[0].twoFactorEnabled").value(false))
                .andExpect(jsonPath("$[0].enabledAccount").value(true))
                .andExpect(jsonPath("$[0].role").value("USER"))

                // --- user2 ---
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].email").value("user2@user.com"))
                .andExpect(jsonPath("$[1].bannedAccount").value(false))
                .andExpect(jsonPath("$[1].twoFactorEnabled").value(false))
                .andExpect(jsonPath("$[1].enabledAccount").value(true))
                .andExpect(jsonPath("$[1].role").value("USER"));



        verify(moderatorService).getAllUsers();
    }

    @Test
    void shouldForbidNonModeratorToGetUserList() throws Exception{

        mockMvc.perform(get("/api/moderator/users")
                .with(user("noModUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }
}
