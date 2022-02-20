package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.MethodArgumentNotValidException;
import com.example.tiktokproject.model.dto.postDTO.PostLikedDTO;
import com.example.tiktokproject.model.dto.userDTO.*;
import com.example.tiktokproject.model.pojo.User;
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
import java.util.List;

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

    @PutMapping("/users/edit")
    public ResponseEntity<UserEditResponseDTO> editUser(@RequestBody UserEditRequestDTO userDTO, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(),userDTO.getId());
        return new ResponseEntity<>(userService.editUser(userDTO), HttpStatus.ACCEPTED);
    }

    @PutMapping("/users/{id}/edit/profilePicture")
    public ResponseEntity<UserEditProfilePictureResponseDTO> editProfilePicture(@PathVariable int id, @RequestParam MultipartFile file, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), id);
        return new ResponseEntity<>(userService.editProfilePicture(file, id), HttpStatus.ACCEPTED);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserInformationDTO> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.ACCEPTED);
    }

    @PostMapping("/users/{id}/follow")
    public ResponseEntity<String> follow(@PathVariable(name = "id") int followedToUserId, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User follower = sessionManager.getSessionUser(request.getSession());
        userService.follow(follower, followedToUserId);
        return new ResponseEntity<>("Your follow request was successful", HttpStatus.ACCEPTED);
    }

    @PostMapping("/users/{id}/unfollow")
    public ResponseEntity<String> unfollow(@PathVariable(name = "id") int unfollowedUserId, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User userWhoWantToUnfollow = sessionManager.getSessionUser(request.getSession());
        userService.unfollow(userWhoWantToUnfollow, unfollowedUserId);
        return new ResponseEntity<>("Your unfollow request was successful", HttpStatus.ACCEPTED);
    }

    @GetMapping("/user/{id}/likedPosts")
    public ResponseEntity<List<PostLikedDTO>> getAllLikedPost(@PathVariable int id, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), id);
        return new ResponseEntity<>(userService.getAllLikedPosts(id), HttpStatus.OK);
    }

    @PostMapping("/users/{id}/setUsername")
    public ResponseEntity<UserSetUsernameDTO> setUsername(@PathVariable int id,@Valid  @RequestBody UserSetUsernameDTO userDto, BindingResult result){
        if (result.hasErrors()){
            throw new MethodArgumentNotValidException("Wrong username or name");
        }
        return new ResponseEntity<>(userService.setUsername(id,userDto.getUsername(),userDto.getName()),HttpStatus.ACCEPTED);
    }


}
