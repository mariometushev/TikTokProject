package com.example.tiktokproject.services;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Value(value = "${spring.mail.username}")
    private String adminEmail;
    private static final String BODY = "In order to complete your registration " +
            "please click on the following link: localhost:9999/verify/";
    private static final String TOPIC = "Registration";

    public void sendSimpleMessage(String userEmail, LocalDateTime registerDate) {
        SimpleMailMessage message = new SimpleMailMessage();
        String token = generateToken(userEmail, registerDate);
        message.setFrom(adminEmail);
        message.setTo(userEmail);
        message.setSubject(TOPIC);
        message.setText(BODY + token);
        emailSender.send(message);
    }

    public String generateToken(String email, LocalDateTime register) {
        String token = email + register.toString();
        return MD5Encoder.encode(token.getBytes(StandardCharsets.UTF_8));//always lead to same result
    }

}
