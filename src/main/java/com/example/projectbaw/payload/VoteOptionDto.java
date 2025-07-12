package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
public class VoteOptionDto {

    @Getter
    @Setter
    public static class RequestDto {

        private String name;
        private byte[] imageData;
        private Long voteId;

    }

    @Getter
    @Setter
    public static class ResponseDto {

        private Long id;
        private String name;
        private int count;
        private byte[] imageData;
        private Long voteId;

    }

    @Getter
    @Setter
    public static class UpdateCountDto {

        private Long id;
    }
}

