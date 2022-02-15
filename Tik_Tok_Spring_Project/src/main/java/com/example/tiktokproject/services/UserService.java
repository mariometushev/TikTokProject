package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundUserException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User loginWithPhone(String phone, String password) {
        if (phone == null || phone.isBlank()){
            throw new BadRequestException("Password is mandatory");
        }
        if (password == null || password.isBlank()){
            throw new BadRequestException("Password is mandatory");
        }
        if (!passwordEncoder.matches(password, userRepository.findPasswordByPhone_number(phone))){
            throw new NotFoundUserException("Wrong email or password!");
        }
        User u = userRepository.findByPhone_number(phone);
        if (u == null){
            throw new NotFoundUserException("Wrong phone number or password!");
        }
        return u;
    }

    public User loginWithEmail(String email, String password) {
        if (password == null || password.isBlank()){
            throw new BadRequestException("Password is mandatory!");
        }
        if (email.matches("^(.+)@(.+)$")){
            throw new BadRequestException("The email is not valid!");
        }
        if (email.isBlank()){
            throw new BadRequestException("Email is mandatory!");
        }
        if (!passwordEncoder.matches(password, userRepository.findPasswordByEmail(email))){
            throw new NotFoundUserException("Wrong email or password!");
        }
        User u = userRepository.findByEmail(email);
        if (u == null){
            throw new NotFoundUserException("Wrong email or password!");
        }
        return u;
    }
}
