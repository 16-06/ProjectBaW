package com.example.projectbaw.payload;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class WhoVotedYetDto {

    public static class RequestDto {
        private Long userId;
        private Long voteId;
        private boolean alreadyVoted;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getVoteId() { return voteId; }
        public void setVoteId(Long voteId) { this.voteId = voteId; }

        public boolean isAlreadyVoted() { return alreadyVoted; }
        public void setAlreadyVoted(boolean alreadyVoted) { this.alreadyVoted = alreadyVoted; }
    }

    public static class ResponseDto {
        private Long id;
        private Long userId;
        private Long voteId;
        private boolean alreadyVoted;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getVoteId() { return voteId; }
        public void setVoteId(Long voteId) { this.voteId = voteId; }

        public boolean isAlreadyVoted() { return alreadyVoted; }
        public void setAlreadyVoted(boolean alreadyVoted) { this.alreadyVoted = alreadyVoted; }
    }
}