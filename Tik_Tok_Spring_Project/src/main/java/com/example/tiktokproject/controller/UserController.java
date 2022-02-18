package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.MethodArgumentNotValidException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.userDTO.*;
import com.example.tiktokproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class UserController {

    public static final String LOGGED = "logged";
    public static final String LOGGED_FROM = "logged_from";
    public static final String USER_ID = "user_id";

    @Autowired
    private UserService userService;

    @PostMapping("/loginWithPhone")
    public ResponseEntity<UserLoginResponseWithPhoneDTO> login(@Valid @RequestBody UserLoginWithPhoneDTO user, HttpSession session, HttpServletRequest request, BindingResult result) {
        if(result.hasErrors()){
            throw new MethodArgumentNotValidException("Wrong phone number or password credentials");
        }
        UserLoginResponseWithPhoneDTO dto = userService.loginWithPhone(user);
        session.setAttribute(LOGGED, true);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(USER_ID, dto.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/loginWithEmail")
    public ResponseEntity<UserLoginResponseWithEmailDTO> login(@Valid @RequestBody UserLoginWithEmailDTO user, HttpSession session, HttpServletRequest request, BindingResult result) {
        if(result.hasErrors()){
            throw new MethodArgumentNotValidException("Wrong email or password credentials");
        }
        // TODO change session logic outside
        UserLoginResponseWithEmailDTO dto = userService.loginWithEmail(user);
        session.setAttribute(LOGGED, true);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());
        session.setAttribute(USER_ID, dto.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logOut(HttpSession session) {
        session.invalidate();
    }

    @PostMapping("/registerWithEmail")
    public ResponseEntity<UserRegisterResponseWithEmailDTO> register(@Valid @RequestBody UserRegisterRequestWithEmailDTO userDTO,BindingResult result) {
        if(result.hasErrors()){
            throw new MethodArgumentNotValidException("Wrong email or password credentials");
        }
        return new ResponseEntity<>(userService.registerWithEmail(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/registerWithPhone")
    public ResponseEntity<UserRegisterResponseWithPhoneDTO> register(@Valid @RequestBody UserRegisterRequestWithPhoneDTO userDTO,BindingResult result) {
        if(result.hasErrors()){
            throw new MethodArgumentNotValidException("Wrong phone number or password credentials");
        }
        return new ResponseEntity<>(userService.registerWithPhone(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/users/edit")
    public ResponseEntity<UserEditResponseDTO> editUser(@RequestBody UserEditRequestDTO userDTO, HttpServletRequest request) {
        validateLogin(request);
        // TODO change method name and logic
        return new ResponseEntity<>(userService.editUser(userDTO),HttpStatus.OK);
    }

    private void validateLogin(HttpServletRequest request) {
        // TODO remove this method from here
        HttpSession session = request.getSession();
        boolean newSession = session.isNew();
        boolean logged = session.getAttribute(LOGGED) != null && ((Boolean) session.getAttribute(LOGGED));
        boolean sameIP = request.getRemoteAddr().equals(session.getAttribute(LOGGED_FROM));
        if (newSession || !logged || !sameIP) {
            throw new UnauthorizedException("You have to log");
        }
    }


}
