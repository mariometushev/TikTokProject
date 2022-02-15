package com.example.tiktokproject.controller;
import com.example.tiktokproject.model.dto.UserLoginResponseWithEmailDTO;
import com.example.tiktokproject.model.dto.UserLoginWithEmailDTO;
import com.example.tiktokproject.model.dto.UserLoginWithPhoneDTO;
import com.example.tiktokproject.model.dto.UserLoginResponseWithPhoneDTO;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    public static final String LOGGED = "logged";
    public static final String LOGGED_FROM = "logged_from";

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/loginWithPhone")
    public UserLoginResponseWithPhoneDTO login(@RequestBody UserLoginWithPhoneDTO user, HttpSession session, HttpServletRequest request) {
        String phone = user.getPhone();
        String password = user.getPassword();
        User u = userService.loginWithPhone(phone, password);
        session.setAttribute(LOGGED,true);
        session.setAttribute(LOGGED_FROM,request.getRemoteAddr());
        UserLoginResponseWithPhoneDTO dto = modelMapper.map(u,UserLoginResponseWithPhoneDTO.class);
        return dto;
    }

    @PostMapping("/loginWithEmail")
    public UserLoginResponseWithEmailDTO login(@RequestBody UserLoginWithEmailDTO user, HttpSession session, HttpServletRequest request){
        String email = user.getEmail();
        String password = user.getPassword();
        User u = userService.loginWithEmail(email, password);
        session.setAttribute(LOGGED,true);
        session.setAttribute(LOGGED_FROM,request.getRemoteAddr());
        UserLoginResponseWithEmailDTO dto = modelMapper.map(u,UserLoginResponseWithEmailDTO.class);
        return dto;
    }



}
