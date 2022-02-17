package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.userDTO.*;
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

    public UserLoginResponseWithPhoneDTO loginWithPhone(UserLoginWithPhoneDTO user) {
        String phone = user.getPhoneNumber();
        String password = user.getPassword();
        if (phone == null || phone.isBlank()) {
            throw new BadRequestException("Phone number is mandatory");
        }
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is mandatory");
        }
        if (userRepository.findByPhoneNumber(phone).isEmpty()) {
            throw new NotFoundException("Wrong phone number or password!");
        }
        if (!passwordEncoder.matches(password, userRepository.findByPhoneNumber(phone).get().getPassword())) {
            throw new NotFoundException("Wrong phone number or password!");
        }
        User u = userRepository.findByPhoneNumber(phone).get();
        return modelMapper.map(u, UserLoginResponseWithPhoneDTO.class);
    }

    public UserLoginResponseWithEmailDTO loginWithEmail(UserLoginWithEmailDTO user) {
        String email = user.getEmail();
        String password = user.getPassword();
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is mandatory!");
        }
        if (checkForValidEmail(user.getEmail())) {
            throw new BadRequestException("The email is not valid!");
        }
        if (email.isBlank()) {
            throw new BadRequestException("Email is mandatory!");
        }
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new NotFoundException("Wrong email or password!");
        }
        if (!passwordEncoder.matches(password, userRepository.findByEmail(email).get().getPassword())) {
            throw new NotFoundException("Wrong email or password!");
        }
        User u = userRepository.findByEmail(email).get();
        return modelMapper.map(u, UserLoginResponseWithEmailDTO.class);
    }

    public UserRegisterResponseWithEmailDTO registerWithEmail(UserRegisterRequestWithEmailDTO userEmailDTO) {
        if (userRepository.findByEmail(userEmailDTO.getEmail()).isPresent()) {
            throw new BadRequestException("User with this email already exist");
        }
        if (checkForValidEmail(userEmailDTO.getEmail())) {
            throw new BadRequestException("Invalid email address");
        }
        checkForValidPasswordAndDateOfBirth(userEmailDTO.getPassword(), userEmailDTO.getConfirmPassword(), userEmailDTO.getDateOfBirth());
        User u = modelMapper.map(userEmailDTO, User.class);
        u.setPassword(passwordEncoder.encode(userEmailDTO.getPassword()));
        u.setRoleId(1);
        u.setRegisterDate(LocalDateTime.now());
        return modelMapper.map(u, UserRegisterResponseWithEmailDTO.class);
    }

    public UserRegisterResponseWithPhoneDTO registerWithPhone(UserRegisterRequestWithPhoneDTO userPhoneDTO) {
        if (userRepository.findByPhoneNumber(userPhoneDTO.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("User with this phone already exist");
        }
        if (userPhoneDTO.getPhoneNumber().isBlank()) {
            throw new BadRequestException("Phone number is mandatory");
        }
        checkForValidPasswordAndDateOfBirth(userPhoneDTO.getPassword(), userPhoneDTO.getConfirmPassword(), userPhoneDTO.getDateOfBirth());
        User u = modelMapper.map(userPhoneDTO, User.class);
        u.setPassword(passwordEncoder.encode(userPhoneDTO.getPassword()));
        u.setRoleId(1);
        u.setRegisterDate(LocalDateTime.now());
        return modelMapper.map(u, UserRegisterResponseWithPhoneDTO.class);
    }

    public UserEditResponseDTO changeUserEmail(UserEditRequestDTO userEmailDTO) {
        if (userRepository.findById(userEmailDTO.getId()).isEmpty()) {
            throw new BadRequestException("Wrong user id");
        }
        if (userEmailDTO.getEmail().isBlank()) {
            throw new BadRequestException("Email is mandatory");
        }
        User afterChangeEmail = modelMapper.map(userEmailDTO, User.class);
        return modelMapper.map(afterChangeEmail, UserEditResponseDTO.class);
    }

    private boolean checkForValidPassword(String password) {
        return !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}");
    }

    private boolean checkForValidEmail(String email) {
        return !email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+.[a-zA-Z]+$");
    }

    private void checkForValidPasswordAndDateOfBirth(String password, String confirmPassword, LocalDate localDate) {
        if (checkForValidPassword(password)) {
            throw new BadRequestException("Password must contain at least one digit from [0-9]," +
                    " one lower case letter, one upper case letter, one special symbol and length more than seven symbols");
        }
        if (!password.equals(confirmPassword)) {
            throw new BadRequestException("Password and confirm password should be equals");
        }
        if (localDate.isAfter(LocalDate.now().minusYears(13))) {
            throw new UnauthorizedException("You should be at least 13 years old");
        }
    }
}
