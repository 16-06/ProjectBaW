package com.example.projectbaw.integrationTest;

import com.example.projectbaw.enums.ContentType;
import com.example.projectbaw.enums.ResolutionStatus;
import com.example.projectbaw.enums.Role;
import com.example.projectbaw.payload.ReportDto;
import com.example.projectbaw.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReportTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ReportService reportService;


    @Test
    void shouldGetAllReportByAdmin() throws Exception {

        // given
        ReportDto.ResponseDto report1 = new ReportDto.ResponseDto();
        report1.setId(1L);
        report1.setReporterId(10L);
        report1.setContentId(100L);
        report1.setReason("Spam");
        report1.setContentType(ContentType.POST);
        report1.setStatus(ResolutionStatus.PENDING);
        report1.setCreatedAt(LocalDateTime.of(2025, 8, 15, 12, 0));

        ReportDto.ResponseDto report2 = new ReportDto.ResponseDto();
        report2.setId(2L);
        report2.setReporterId(20L);
        report2.setContentId(200L);
        report2.setReason("Offensive language");
        report2.setContentType(ContentType.COMMENT);
        report2.setStatus(ResolutionStatus.ACCEPTED);
        report2.setCreatedAt(LocalDateTime.of(2025, 8, 15, 13, 0));

        when(reportService.findAllReports()).thenReturn(List.of(report1, report2));

        //when + then
        mockMvc.perform(get("/api/report/moderator/reports")
                .with(user("AdminUser").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                // User 1
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].reporterId").value(10))
                .andExpect(jsonPath("$[0].contentId").value(100))
                .andExpect(jsonPath("$[0].reason").value("Spam"))
                .andExpect(jsonPath("$[0].contentType").value("POST"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].createdAt").exists())

                // User 2
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].reporterId").value(20))
                .andExpect(jsonPath("$[1].contentId").value(200))
                .andExpect(jsonPath("$[1].reason").value("Offensive language"))
                .andExpect(jsonPath("$[1].contentType").value("COMMENT"))
                .andExpect(jsonPath("$[1].status").value("ACCEPTED"))
                .andExpect(jsonPath("$[1].createdAt").exists());

        verify(reportService).findAllReports();


    }
    @Test
    void shouldGetAllReportByMod() throws Exception {

        // given
        ReportDto.ResponseDto report1 = new ReportDto.ResponseDto();
        report1.setId(1L);
        report1.setReporterId(10L);
        report1.setContentId(100L);
        report1.setReason("Spam");
        report1.setContentType(ContentType.POST);
        report1.setStatus(ResolutionStatus.PENDING);
        report1.setCreatedAt(LocalDateTime.of(2025, 8, 15, 12, 0));

        ReportDto.ResponseDto report2 = new ReportDto.ResponseDto();
        report2.setId(2L);
        report2.setReporterId(20L);
        report2.setContentId(200L);
        report2.setReason("Offensive language");
        report2.setContentType(ContentType.COMMENT);
        report2.setStatus(ResolutionStatus.ACCEPTED);
        report2.setCreatedAt(LocalDateTime.of(2025, 8, 15, 13, 0));

        when(reportService.findAllReports()).thenReturn(List.of(report1, report2));

        //when + then
        mockMvc.perform(get("/api/report/moderator/reports")
                        .with(user("AdminUser").roles("MODERATOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                // User 1
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].reporterId").value(10))
                .andExpect(jsonPath("$[0].contentId").value(100))
                .andExpect(jsonPath("$[0].reason").value("Spam"))
                .andExpect(jsonPath("$[0].contentType").value("POST"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[0].createdAt").exists())

                // User 2
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].reporterId").value(20))
                .andExpect(jsonPath("$[1].contentId").value(200))
                .andExpect(jsonPath("$[1].reason").value("Offensive language"))
                .andExpect(jsonPath("$[1].contentType").value("COMMENT"))
                .andExpect(jsonPath("$[1].status").value("ACCEPTED"))
                .andExpect(jsonPath("$[1].createdAt").exists());

        verify(reportService).findAllReports();

    }

    @Test
    void shouldForbidNonModeratorToGetAllReport() throws Exception {

        mockMvc.perform(get("/api/report/moderator/reports")
                .with(user("noModUser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

}
