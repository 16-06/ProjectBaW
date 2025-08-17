package com.example.projectbaw.integrationTest;

import com.example.projectbaw.payload.AdminDto;
import com.example.projectbaw.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.temporal.ChronoUnit;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    void shouldAllowAdminToBlock() throws Exception{

        AdminDto.BanUserDto banUserDto = new AdminDto.BanUserDto();
        banUserDto.setUserId(1L);
        banUserDto.setDurationAmount(2L);
        banUserDto.setDurationUnit(ChronoUnit.DAYS);

        mockMvc.perform(put("/api/admin/block-user")
                    .with(user("adminUser").roles("ADMIN"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(banUserDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("User blocked successfully")));

        Mockito.verify(adminService).blockUser(Mockito.eq(banUserDto));
    }


    @Test
    void shouldForbidNonAdminToBlock() throws Exception{

        AdminDto.BanUserDto banUserDto = new AdminDto.BanUserDto();
        banUserDto.setUserId(1L);
        banUserDto.setDurationAmount(2L);
        banUserDto.setDurationUnit(ChronoUnit.DAYS);

        mockMvc.perform(put("/api/admin/block-user")
                        .with(user("noAdminUser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(banUserDto))
                )
                .andExpect(status().isForbidden());


        Mockito.verify(adminService,Mockito.never()).blockUser(Mockito.any());

    }

    @Test
    void shouldAllowAdminToUnblock() throws Exception{

        Long userId = 1L;

        mockMvc.perform(put("/api/admin/unblock-user")
                        .with(user("adminUser").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userId))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("User unblocked successfully")));

        Mockito.verify(adminService).unblockUser(userId);
    }

    @Test
    void shouldForbidNonAdminToUnblock() throws Exception{

        Long userId = 1L;

        mockMvc.perform(put("/api/admin/unblock-user")
                        .with(user("noAdminUser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userId))
                )
                .andExpect(status().isForbidden());


        Mockito.verify(adminService,Mockito.never()).blockUser(Mockito.any());

    }




}
