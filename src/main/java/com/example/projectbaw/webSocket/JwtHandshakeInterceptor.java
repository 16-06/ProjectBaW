package com.example.projectbaw.webSocket;

import com.example.projectbaw.config.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    // This interceptor executes when a WebSocket connection is attempted.
    // Validate the JWT token and potentially reject the connection if the token is invalid.

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

            //String authHeader = request.getHeaders().getFirst("Authorization");

            URI uri = request.getURI();
            String authHeader = uri.getQuery();

            System.out.println(">> Token: " + authHeader); // DEBUG

            if (authHeader != null && authHeader.startsWith("token=")) {
                String token = authHeader.substring(6);

                Claims claims = jwtUtil.praseToken(token);
                String username = claims.get("username", String.class);

                attributes.put("jwtToken", token);
                attributes.put("username", username);

                return true;
            } else {

                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
