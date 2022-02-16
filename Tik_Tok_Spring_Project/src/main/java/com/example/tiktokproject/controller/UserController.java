package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.UserLoginResponseWithEmailDTO;
import com.example.tiktokproject.model.dto.UserLoginWithEmailDTO;
import com.example.tiktokproject.model.dto.UserLoginWithPhoneDTO;
import com.example.tiktokproject.model.dto.UserLoginResponseWithPhoneDTO;
import com.example.tiktokproject.model.dto.*;
import com.example.tiktokproject.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    public static final String LOGGED = "logged";
    public static final String LOGGED_FROM = "logged_from";
    public static final String USER_ID = "user_id";

    @Autowired
    private UserService userService;

    @PostMapping("/loginWithPhone")
    public UserLoginResponseWithPhoneDTO login(@RequestBody UserLoginWithPhoneDTO user, HttpSession session, HttpServletRequest request) {
        UserLoginResponseWithPhoneDTO dto = userService.loginWithPhone(user);
        session.setAttribute(LOGGED, true);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(USER_ID, dto.getId());
        return dto;
    }

    @PostMapping("/loginWithEmail")
    public UserLoginResponseWithEmailDTO login(@RequestBody UserLoginWithEmailDTO user, HttpSession session, HttpServletRequest request) {
        UserLoginResponseWithEmailDTO dto = userService.loginWithEmail(user);
        session.setAttribute(LOGGED, true);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(USER_ID, dto.getId());
        return dto;
    }

    @PostMapping("/logout")
    public void logOut(HttpSession session){
        session.invalidate();
    }

    @PostMapping("/registerWithEmail")
    public ResponseEntity<UserRegisterResponseWithEmailDTO> registerWithEmail(@RequestBody UserRegisterRequestWithEmailDTO userDTO) {
        UserRegisterResponseWithEmailDTO returnUserToResponse = userService.registerWithEmail(userDTO);
        return new ResponseEntity<>(returnUserToResponse, HttpStatus.CREATED);
    }

    @PostMapping("/registerWithPhone")
    public ResponseEntity<UserRegisterResponseWithPhoneDTO> registerWithEmail(@RequestBody UserRegisterResponseWithPhoneDTO userDTO) {
        UserRegisterResponseWithPhoneDTO returnUserToResponse = userService.registerWithPhone(userDTO);
        return new ResponseEntity<>(returnUserToResponse, HttpStatus.CREATED);
    }

    private void validateLogin(HttpSession session, HttpServletRequest request){
        if (session.isNew() ||
                (!(Boolean) session.getAttribute(LOGGED)) ||
                (!request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM)))){
                throw new UnauthorizedException("You have to login!");
        }
    }

}
