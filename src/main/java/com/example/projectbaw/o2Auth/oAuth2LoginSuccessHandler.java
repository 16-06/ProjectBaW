package com.example.projectbaw.o2Auth;

import com.example.projectbaw.config.JwtUtil;
import com.example.projectbaw.model.User;
import com.example.projectbaw.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class oAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.frontend-url}")
    private String frontUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found "));

        String token = jwtUtil.generateToken(user);


        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", frontUrl);

        Cookie cookie = new Cookie("token", token);

        cookie.setHttpOnly(false);  // Only for testing purposes, both should be true in production
        cookie.setSecure(false);    // But in frontend wasn't fully modified to work with cookies
                                    // JWT token is extracted from cookies and stored in localStorage

                                    // This is not secure, but it is a temporary solution and token should fully store in cookies
                                    // This is because I did not write frontend from scratch like this project
                                    // Only modified the old version of the React project
        cookie.setPath("/");
        cookie.setMaxAge(3600*24);
        response.addCookie(cookie);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\": \"ok\"}");

        //response.sendRedirect(frontUrl);
    }
}
