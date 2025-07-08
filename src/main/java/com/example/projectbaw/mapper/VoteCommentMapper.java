package com.example.projectbaw.mapper;

import com.example.projectbaw.model.VoteComment;
import com.example.projectbaw.payload.VoteCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteCommentMapper {


    @Mapping(source = "vote.id", target = "voteId")
    @Mapping(source = "commentAuthor.id", target = "commentAuthorId")
    @Mapping(source = "commentAuthor.username", target = "commentAuthorUsername")
    VoteCommentDto.ResponseDto toResponseDto(VoteComment voteComment);

    @Mapping(target = "vote", ignore = true)
    @Mapping(target = "commentAuthor", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    VoteComment toEntity(VoteCommentDto.RequestDto requestDto);
}

//              Normal mapper, but I will try to use MapStruct for education purposes

//@Component
//@RequiredArgsConstructor
//public class VoteCommentMapper {

//    public VoteCommentDto.ResponseDto toVoteCommentDto(VoteComment voteComment) {
//        if (voteComment == null) {
//            return null;
//        }
//
//        VoteCommentDto.ResponseDto responseDto = new VoteCommentDto.ResponseDto();
//        responseDto.setId(voteComment.getId());
//        responseDto.setVoteId(voteComment.getVote().getId());
//        responseDto.setCommentAuthor(voteComment.getCommentAuthor().getUsername());
//        responseDto.setCommentBody(voteComment.getCommentBody());
//        responseDto.setCreatedAt(voteComment.getCreatedAt());
//
//        return responseDto;
//    }
//
//    public VoteComment toEntity(VoteCommentDto.RequestDto requestDto) {
//        if (requestDto == null) {
//            return null;
//        }
//
//        VoteComment voteComment = new VoteComment();
//        voteComment.setCommentBody(requestDto.getCommentBody());
//        voteComment.setId(requestDto.getVoteId());
//
//        return voteComment;
//    }}
