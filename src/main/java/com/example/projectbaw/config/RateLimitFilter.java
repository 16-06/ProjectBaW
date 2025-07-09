package com.example.projectbaw.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket(int limit, Duration duration) {

        Bandwidth limitBandwidth = Bandwidth.classic(limit, Refill.intervally(limit, duration));

        return Bucket4j.builder().addLimit(limitBandwidth).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth != null && auth.isAuthenticated() &&
                auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        //  Rate limiter by
        //  - per IP address
        //  - per Endpoint
        //  - per User

        String ip = request.getRemoteAddr();
        String endpoint = request.getRequestURI();
        String username = (auth != null && auth.isAuthenticated()) && !"anonymousUser".equals(auth.getName()) ? auth.getName() : null;

        String ipKey = "IP:" + ip;
        String userKey = username != null ? "USER:" + username : null;
        String endpointKey = endpoint != null ? "ENDPOINT:" + endpoint : null;

        int ipLimit = isAdmin ? 200 : 20;
        int endpointLimit = isAdmin ? 100 : 10;
        int userLimit = isAdmin ? 50 : 15;

        Bucket ipBucket =                       buckets.computeIfAbsent(ipKey, k -> createNewBucket(ipLimit, Duration.ofMinutes(1)));
        Bucket endPointBucket =                 buckets.computeIfAbsent(endpointKey, k -> createNewBucket(endpointLimit, Duration.ofMinutes(1)));
        Bucket userBucket = userKey != null ?   buckets.computeIfAbsent(userKey, k -> createNewBucket(userLimit, Duration.ofMinutes(1))) : null;

        boolean allowedIp = ipBucket.tryConsume(1);
        boolean allowedEndpoint = endPointBucket.tryConsume(1);
        boolean allowedUser = userBucket == null || userBucket.tryConsume(1);



        if (allowedIp && allowedEndpoint && allowedUser) {

            filterChain.doFilter(request, response);
        }
        else {

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Try again later.");
        }
    }
}
