package com.example.tiktokproject.services;

import com.example.tiktokproject.model.pojo.Token;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.TokenRepository;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Value(value = "${spring.mail.username}")
    private String adminEmail;
    public static final String REGISTRATION_BODY = "In order to complete your registration " +
            "please click on the following link: localhost:9999/verify/";
    public static final String REGISTRATION_TOPIC = "Registration";
    public static final String PASSWORD_BODY = "In order to reset your password " +
            "please click on the following link: localhost:9999/forgottenPassword/";
    public static final String PASSWORD_TOPIC = "Forgotten password";


    public void sendSimpleMessage(User user, String body, String topic) {
        SimpleMailMessage message = new SimpleMailMessage();
        Token token = new Token();
        String tokenName = generateToken();
        token.setToken(tokenName);
        token.setOwner(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(Token.EXPIRATION));
        tokenRepository.save(token);
        message.setFrom(adminEmail);
        message.setTo(user.getEmail());
        message.setSubject(topic);
        message.setText(body + token);
        emailSender.send(message);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

}
