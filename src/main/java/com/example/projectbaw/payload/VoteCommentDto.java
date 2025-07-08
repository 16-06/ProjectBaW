package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class VoteCommentDto {


    @Getter
    @Setter
    public static class RequestDto {

        private String commentBody;
        private Long voteId;
        private Long commentAuthorId;

    }

    @Getter
    @Setter
    public static class ResponseDto {

        private Long id;
        private String commentBody;
        private Long commentAuthorId;
        private Long voteId;
        private LocalDateTime createdAt;
        private String commentAuthorUsername;

    }
}
