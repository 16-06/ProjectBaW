package com.example.projectbaw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String email, String token) {

        String subject = "Account Activation";
        String confirmationLink = "http://localhost:8080/api/users/confirm?token=" + token;
        String body = "Activation account link: " + confirmationLink;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String email, String token) {

        String subject = "Password Reset Request";
        String link  = "http://localhost:3000/api/users/reset-password?token=" + token;
        String body = "Password Reset Request link: " + link ;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }


    public void sendTwoFactorCode(String email, String code) {

        String subject = "2FA Code";
        String link = "http://localhost:3000/2fa/verify?token=" + code;
        String body = "Click this link to complete login:" + link + " Link expires in 5 minutes.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
