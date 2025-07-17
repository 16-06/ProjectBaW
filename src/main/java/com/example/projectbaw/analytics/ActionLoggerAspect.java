package com.example.projectbaw.analytics;

import com.example.projectbaw.model.Vote;
import com.example.projectbaw.model.VoteComment;
import com.example.projectbaw.model.VoteOption;
import com.example.projectbaw.payload.VoteCommentDto;
import com.example.projectbaw.payload.VoteDto;
import com.example.projectbaw.payload.VoteOptionDto;
import com.example.projectbaw.service.UserActionLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
@RequiredArgsConstructor
public class ActionLoggerAspect {

    private final UserActionLogService logService;

    @Around("@annotation(trackAction)")
    public Object logAction(ProceedingJoinPoint joinPoint, TrackAction trackAction) throws Throwable {
        Object result = joinPoint.proceed();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return result;}


        String username = authentication.getName();
        String details = generateDynamicDetails(joinPoint.getArgs(), trackAction.details());

        logService.log(username, trackAction.value(), details);

        return result;
    }


    private String generateDynamicDetails(Object[] args, String defaultDetail) {

        for(Object arg : args) {

            if (arg instanceof VoteDto.RequestDto vote) {
                return "Vote : " + vote.getName();
            }
            else if (arg instanceof VoteCommentDto.RequestDto comment) {

                String commentText = comment.getCommentBody().length() <= 10 ? comment.getCommentBody() : comment.getCommentBody().substring(0, 10) + "...";

                return "Comment from: " + commentText;
            }
            else if (arg instanceof VoteOptionDto.RequestDto option) {
                return "Vote option : " + option.getName();
            }
        }

        return defaultDetail;
    }

}
