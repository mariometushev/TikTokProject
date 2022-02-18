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
        checkForInValidPasswordAndDateOfBirth(userEmailDTO.getPassword(), userEmailDTO.getConfirmPassword(), userEmailDTO.getDateOfBirth());
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
        checkForInValidPasswordAndDateOfBirth(userPhoneDTO.getPassword(), userPhoneDTO.getConfirmPassword(), userPhoneDTO.getDateOfBirth());
        User u = modelMapper.map(userPhoneDTO, User.class);
        u.setPassword(passwordEncoder.encode(userPhoneDTO.getPassword()));
        u.setRoleId(1);
        u.setRegisterDate(LocalDateTime.now());
        return modelMapper.map(u, UserRegisterResponseWithPhoneDTO.class);
    }

    public UserEditResponseDTO editUser(UserEditRequestDTO userEmailDTO) {
        // TODO change method logic
        if (userRepository.findById(userEmailDTO.getId()).isEmpty()) {
            throw new BadRequestException("Wrong user id");
        }
        User afterChangeEmail = modelMapper.map(userEmailDTO, User.class);
        return modelMapper.map(afterChangeEmail, UserEditResponseDTO.class);
    }

    private void checkForInValidPasswordAndDateOfBirth(String password, String confirmPassword, LocalDate localDate) {
        if (!password.equals(confirmPassword)) {
            throw new BadRequestException("Password and confirm password should be equals");
        }
        if (localDate.isAfter(LocalDate.now().minusYears(13))) {
            throw new UnauthorizedException("You should be at least 13 years old");
        }
    }
}
