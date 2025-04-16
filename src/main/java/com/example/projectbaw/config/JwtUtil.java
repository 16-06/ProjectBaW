package com.example.projectbaw.config;

import com.example.projectbaw.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secretKey = "secretKey";
    private final long validityInMs = 1000 * 60 * 60 * 24; //24H

    public String generateToken(User user) {
        return Jwts.builder()
                .claim("username",user.getUsername())
                .claim("id",user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512,secretKey.getBytes())
                .compact();

    }
    public Claims praseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

}
