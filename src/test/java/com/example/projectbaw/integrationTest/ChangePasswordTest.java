package com.example.projectbaw.integrationTest;

import com.example.projectbaw.payload.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChangePasswordTest extends BaseTestUser {

    @Test
    @WithMockUser(username = "testUser")
    void shouldChangePassword() throws Exception {

        UserDto.ChangePassDto dto = new UserDto.ChangePassDto();
        dto.setPassword("password123");
        dto.setNewPassword("newSecret123");

        mockMvc.perform(put("/api/users/changePassword")
                        .contentType(JSON)
                        .content(json(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john")
    void shouldNotChangePasswordWithWrongOldPassword() throws Exception {


        String body = """
                    {"password": "bad", "newPassword": "newPass123"}
                """;

        mockMvc.perform(put("/api/users/changePassword")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }
}
