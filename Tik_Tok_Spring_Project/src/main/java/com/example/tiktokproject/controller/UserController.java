package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.userDTO.*;
import com.example.tiktokproject.services.UserService;
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
    public ResponseEntity<UserLoginResponseWithPhoneDTO> login(@RequestBody UserLoginWithPhoneDTO user, HttpSession session, HttpServletRequest request) {
        UserLoginResponseWithPhoneDTO dto = userService.loginWithPhone(user);
        session.setAttribute(LOGGED, true);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(USER_ID, dto.getId());
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @PostMapping("/loginWithEmail")
    public ResponseEntity<UserLoginResponseWithEmailDTO> login(@RequestBody UserLoginWithEmailDTO user, HttpSession session, HttpServletRequest request) {
        UserLoginResponseWithEmailDTO dto = userService.loginWithEmail(user);
        session.setAttribute(LOGGED, true);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(USER_ID, dto.getId());
        return new ResponseEntity<>(dto,HttpStatus.OK);
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
    public ResponseEntity<UserRegisterResponseWithPhoneDTO> registerWithEmail(@RequestBody UserRegisterRequestWithPhoneDTO userDTO) {
        UserRegisterResponseWithPhoneDTO returnUserToResponse = userService.registerWithPhone(userDTO);
        return new ResponseEntity<>(returnUserToResponse, HttpStatus.CREATED);
    }

    private void validateLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean) session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if (newSession || !logged || sameIP){
            throw new UnauthorizedException("You have to log!");
        }
    }

}
