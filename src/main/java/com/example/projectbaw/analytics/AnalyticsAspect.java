package com.example.projectbaw.analytics;

import com.example.projectbaw.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AnalyticsAspect {

    private final AnalyticsService analyticsService;

    @Around("@annotation(trackAction)")
    public Object logAction(ProceedingJoinPoint joinPoint, TrackAction trackAction) throws Throwable {

        Object result = joinPoint.proceed();
        String action = trackAction.value();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : "anonymous";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String ip = request.getRemoteAddr();
        String endpoint = request.getRequestURI();

        analyticsService.logEvent(action,username,ip,endpoint);

        return result;
    }


}
