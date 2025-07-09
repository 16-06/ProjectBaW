package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@RequiredArgsConstructor
public class VoteDto {

    @Getter
    @Setter
    public static class RequestDto {

        private String name;
        private String author;
        private String category;
        private byte[] imageData;
        private List<VoteOptionDto.RequestDto> options;

    }

    @Getter
    @Setter
    public static class ResponseDto {
        private Long id;
        private String name;
        private String author;
        private String category;
        private byte[] imageData;
        private List<VoteOptionDto.ResponseDto> options;
    }

}
