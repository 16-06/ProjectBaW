package com.example.projectbaw.webSocket;

import com.example.projectbaw.config.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    // Executes when a WebSocket connection is attempted.
    // Validate the JWT token and potentially reject the connection if the token is invalid.

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            Cookie[] cookies = servletRequest.getCookies();

            if(cookies != null) {
                Optional<String> CookieToken = Arrays.stream(cookies)
                        .filter(cookie -> "jwt".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst();

            String token = CookieToken.get();


            Claims claims = jwtUtil.praseToken(token);
            String username = claims.get("username", String.class);

            attributes.put("jwtToken", token);
            attributes.put("username", username);

            return true;

            }

            else {

                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }



    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
