package com.example.projectbaw.webSocket;

import com.example.projectbaw.config.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@RequiredArgsConstructor
public class JwtAuthChannelInterceptor implements ChannelInterceptor {

    // Channel interceptor that operates at the STOMP level
    // Operates after handshake, on WebSocket communication.

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(accessor.getCommand() != null && accessor.getCommand().toString().equals("CONNECT")){

            String authHeader  = accessor.getFirstNativeHeader("Authorization");

            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                try {
                    Claims claims = jwtUtil.praseToken(token);
                    String username = claims.get("username", String.class);
                    String role = claims.get("role", String.class);

                    List<GrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority("ROLE_" + role));

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    accessor.setUser(auth);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid token in WebSocket connection");
                }

            }
        }

        return message;
    }
}
