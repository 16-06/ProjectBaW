package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
public class WhoVotedYetDto {

    @Getter
    @Setter
    public static class RequestDto {

        private Long voteId;
        private boolean alreadyVoted;

    }

    @Getter
    @Setter
    public static class ResponseDto {
        private Long id;
        private Long userId;
        private Long voteId;
        private boolean alreadyVoted;

    }
}