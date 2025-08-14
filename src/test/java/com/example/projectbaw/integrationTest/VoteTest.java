package com.example.projectbaw.integrationTest;

import com.example.projectbaw.payload.VoteDto;
import com.example.projectbaw.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class VoteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    @Test
    void shouldGetAllVote() throws Exception{
        //given

        VoteDto.ResponseDto dto1 = new VoteDto.ResponseDto();
        dto1.setId(1L);
        dto1.setAuthor("test");
        dto1.setCategory("test");
        dto1.setName("test");

        VoteDto.ResponseDto dto2 = new VoteDto.ResponseDto();
        dto2.setId(2L);
        dto2.setAuthor("test2");
        dto2.setCategory("test2");
        dto2.setName("test2");


        List<VoteDto.ResponseDto> votes = List.of(dto1,dto2);

        Mockito.when(voteService.getAllVotes()).thenReturn(votes);

        //when + then

        mockMvc.perform(get("/api/vote/public/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andDo(result -> log.info("{}",result.getResponse().getContentAsString()));
    }



    @Test
    void shouldGetVotesByCategory() throws Exception {
        //given

        VoteDto.ResponseDto dto1 = new VoteDto.ResponseDto();
        dto1.setId(1L);
        dto1.setAuthor("test");
        dto1.setCategory("Category1");
        dto1.setName("test");

        List<VoteDto.ResponseDto> votesByCategory = List.of(dto1);

        Mockito.when(voteService.getByCategory("Category1")).thenReturn(votesByCategory);

        //when + then

        mockMvc.perform(get("/api/vote/public/category")
                .param("category", "Category1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(result -> log.info("{}",result.getResponse().getContentAsString()));

    }

    @Test
    void shouldReturnNotFound() throws Exception {
        Mockito.when(voteService.getByVoteId(9999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/vote/byId/9999")
                        .with(user("testUser")))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldCreateVote() throws Exception{
        //given
        VoteDto.RequestDto requestDto = new VoteDto.RequestDto();
        requestDto.setCategory("test");
        requestDto.setName("test");

        VoteDto.ResponseDto responseDto = new VoteDto.ResponseDto();
        responseDto.setId(1L);
        responseDto.setAuthor("testUser");
        responseDto.setCategory("test");
        responseDto.setName("test");

        Mockito.when(voteService.createVote(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        // when + then
        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                        .with(user("testUser")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.category").value("test"))
                .andExpect(jsonPath("$.author").value("testUser"));

    }

    @Test
    void shouldDeleteVote() throws Exception{

        //given
        Long voteId = 1L;

        Mockito.doNothing().when(voteService).deleteById(Mockito.anyLong(),Mockito.any());

        // when + then
        mockMvc.perform(delete("/api/vote/{id}", voteId)
                .with(user("testUser")))
                .andExpect(content().string("Vote deleted"));


        Mockito.verify(voteService,Mockito.times(1))
                .deleteById(Mockito.eq(voteId),Mockito.any());
    }

    @Test
    void shouldReturnAuthorId() throws Exception{
        Long voteId = 1L;
        Long userId = 1L;

        Mockito.when(voteService.getAuthorUserId(Mockito.eq(voteId),Mockito.any()))
                .thenReturn(userId);

        mockMvc.perform(get("/api/vote/getAuthorId/{voteId}",voteId)
                        .with(user("testUser").password("pass").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string(userId.toString()));

        Mockito.verify(voteService,Mockito.times(1))
                .getAuthorUserId(Mockito.eq(voteId),Mockito.any());
    }


}
