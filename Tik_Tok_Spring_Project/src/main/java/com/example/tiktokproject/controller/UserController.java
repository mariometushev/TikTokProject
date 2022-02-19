package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.MethodArgumentNotValidException;
import com.example.tiktokproject.model.dto.userDTO.*;
import com.example.tiktokproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/loginWithPhone")
    public ResponseEntity<UserLoginResponseWithPhoneDTO> login(@Valid @RequestBody UserLoginWithPhoneDTO user, HttpSession session, HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException("Wrong phone number or password credentials");
        }
        UserLoginResponseWithPhoneDTO dto = userService.loginWithPhone(user);
        sessionManager.setSession(request, dto.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/loginWithEmail")
    public ResponseEntity<UserLoginResponseWithEmailDTO> login(@Valid @RequestBody UserLoginWithEmailDTO user, HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException("Wrong email or password credentials");
        }
        // TODO change session logic outside
        UserLoginResponseWithEmailDTO dto = userService.loginWithEmail(user);
        sessionManager.setSession(request, dto.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logOut(HttpSession session) {
        session.invalidate();
    }

    @PostMapping("/registerWithEmail")
    public ResponseEntity<UserRegisterResponseWithEmailDTO> register(@Valid @RequestBody UserRegisterRequestWithEmailDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException("Wrong email or password credentials");
        }
        return new ResponseEntity<>(userService.registerWithEmail(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/registerWithPhone")
    public ResponseEntity<UserRegisterResponseWithPhoneDTO> register(@Valid @RequestBody UserRegisterRequestWithPhoneDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException("Wrong phone number or password credentials");
        }
        return new ResponseEntity<>(userService.registerWithPhone(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}/edit")
    public ResponseEntity<UserEditResponseDTO> editUser(@PathVariable int id, @RequestBody UserEditRequestDTO userDTO, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(),id);
        return new ResponseEntity<>(userService.editUser(userDTO), HttpStatus.ACCEPTED);
    }

    @PutMapping("/users/{id}/edit/profilePicture")// TODO get user id and check with session one
    public ResponseEntity<UserEditProfilePictureResponseDTO> editProfilePicture(@PathVariable int id, @RequestParam MultipartFile file, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), id);
        return new ResponseEntity<>(userService.editProfilePicture(file, id), HttpStatus.ACCEPTED);
    }

}
