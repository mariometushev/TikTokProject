package com.example.tiktokproject.controller;

import com.example.tiktokproject.model.dto.UserLoginDTO;
import com.example.tiktokproject.model.dto.UserLoginResponseDTO;
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
    public UserLoginResponseDTO login (@RequestBody UserLoginDTO user, HttpSession session, HttpServletRequest request){
        String phone = user.getPhone();
        String password = user.getPassword();
        User u = userService.loginWithPhone(phone, password);
        return null;
    }

}
