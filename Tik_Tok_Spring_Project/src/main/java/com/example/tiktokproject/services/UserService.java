package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.UserRegisterRequestWithEmailDTO;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    public User loginWithPhone(String phone, String password) {
        if (phone == null || phone.isBlank()){
            throw new BadRequestException("Password is mandatory");
        }
        if (password == null || password.isBlank()){
            throw new BadRequestException("Password is mandatory");
        }
        if (!passwordEncoder.matches(password, userRepository.findPasswordByPhone_number(phone))){
            throw new NotFoundException("Wrong email or password!");
        }
        User u = userRepository.findByPhone_number(phone);
        if (u == null){
            throw new NotFoundException("Wrong phone number or password!");
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
            throw new NotFoundException("Wrong email or password!");
        }
        User u = userRepository.findByEmail(email);
        if (u == null){
            throw new NotFoundException("Wrong email or password!");
        }
        return u;
    }

    public User registerWithEmail(UserRegisterRequestWithEmailDTO userEmailDTO) {
        if (userEmailDTO.getEmail().matches("^(.+)@(.+)$") && userRepository.findByEmail(userEmailDTO.getEmail()) != null) {
            throw new BadRequestException("User with this email already exist");
        }
        boolean checkEmailPass = checkForValidPassword(userEmailDTO.getPassword());
        if (checkEmailPass) {
            throw new BadRequestException("Password must contain at least one digit from [0-9]," +
                    " one lower case letter, one upper case letter, one special symbol and length more than seven symbols");
        }
        if (!userEmailDTO.getPassword().equals(userEmailDTO.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password should be equals");
        }
        if (userEmailDTO.getDate_of_birth().isAfter(LocalDate.now().minusYears(13))) {
            throw new UnauthorizedException("You should be at least 13 years old");
        }
        User u = modelMapper.map(userEmailDTO, User.class);
        u.setPassword(passwordEncoder.encode(userEmailDTO.getPassword()));
        u.setRole_id(1);
        u.setRegister_date(LocalDateTime.now());
        return u;
    }

    private boolean checkForValidPassword(String password) {
        return !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}");
    }

}
