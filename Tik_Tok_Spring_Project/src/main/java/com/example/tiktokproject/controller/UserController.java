package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.model.dto.postDTO.PostLikedDTO;
import com.example.tiktokproject.model.dto.userDTO.*;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/loginWithEmail")
    public ResponseEntity<UserLoginResponseWithEmailDTO> login(@Valid @RequestBody UserLoginWithEmailDTO user,
                                                               HttpServletRequest request) {
        UserLoginResponseWithEmailDTO dto = userService.loginWithEmail(user);
        sessionManager.setSession(request, dto.getId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logOut(HttpSession session) {
        session.invalidate();
    }

    @PostMapping("/registerWithEmail")
    public ResponseEntity<UserRegisterResponseWithEmailDTO> register(@Valid @RequestBody UserRegisterRequestWithEmailDTO userDTO,
                                                                     HttpServletRequest request) {
        UserRegisterResponseWithEmailDTO user = userService.registerWithEmail(userDTO);
        sessionManager.setSession(request, user.getId());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token, HttpServletRequest request) {
        User user = sessionManager.getSessionUser(request.getSession());
        userService.verifyEmail(token, user);
        return new ResponseEntity<>("Email verified.", HttpStatus.ACCEPTED);
    }

    @PostMapping("/forgottenPassword")
    public ResponseEntity<String> sendEmailForForgottenPassword(@Valid @RequestBody UserForgottenPasswordRequestDTO userDto,
                                                                HttpSession session) {
        if(sessionManager.isUserLogged(session)){
            throw new BadRequestException("You are already logged");
        }
        userService.sendEmailForForgottenPassword(userDto);
        return new ResponseEntity<>("Email was send successfully.", HttpStatus.OK);
    }

    @GetMapping("/forgottenPassword/{token}")
    public ResponseEntity<String> checkForgottenPasswordEmailToken(@PathVariable String token, HttpServletRequest request) {
        sessionManager.setSessionUserId(request, userService.getUserIdByToken(token));
        userService.forgottenPassword(token);
        return new ResponseEntity<>("Valid token, please enter your new password.", HttpStatus.OK);
    }

    @PutMapping("/forgottenPassword/changePassword")
    public ResponseEntity<UserEditResponseDTO> changeForgottenPassword(@Valid @RequestBody UserForgottenPasswordDTO userDto,
                                                                       HttpServletRequest request) {
        User user = sessionManager.getSessionUser(request.getSession());
        return new ResponseEntity<>(userService.validateNewPassword(userDto, user), HttpStatus.ACCEPTED);
    }


    @PutMapping("/users/edit")
    public ResponseEntity<UserEditResponseDTO> editUser(@Valid @RequestBody UserEditRequestDTO userDTO,
                                                        HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), userDTO.getId());
        return new ResponseEntity<>(userService.editUser(userDTO), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/users/{id}/edit/profilePicture")
    public ResponseEntity<UserEditProfilePictureResponseDTO> editProfilePicture(@PathVariable int id, @RequestParam(name = "file") MultipartFile file, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), id);
        return new ResponseEntity<>(userService.editProfilePicture(file, id), HttpStatus.ACCEPTED);
    }

    @GetMapping("/users")
    public ResponseEntity<UserInformationDTO> getUserByUsername(@RequestParam(value = "username", defaultValue = "") String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserUsernameDTO>> searchUserByUsername(@RequestParam(value = "search", defaultValue = "") String search,
                                                                      @RequestParam(name = "pageNumber",defaultValue = "0") int pageNumber,
                                                                      @RequestParam(name = "rowsNumber",defaultValue = "10") int rowsNumber) {
        return new ResponseEntity<>(userService.getAllUsersByUsername(search,pageNumber,rowsNumber), HttpStatus.ACCEPTED);
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
    public ResponseEntity<List<PostLikedDTO>> getAllLikedPost(@PathVariable int id,
                                                              @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                              @RequestParam(name = "rowsNumber", defaultValue = "10") int rowsNumber,
                                                              HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), id);
        return new ResponseEntity<>(userService.getAllLikedPosts(id, pageNumber, rowsNumber), HttpStatus.OK);
    }

    @PostMapping("/users/{id}/setUsername")
    public ResponseEntity<UserSetUsernameDTO> setUsername(HttpServletRequest request, @PathVariable int id,
                                                          @Valid @RequestBody UserSetUsernameDTO userDto) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), id);
        return new ResponseEntity<>(userService.setUsername(id, userDto.getUsername(), userDto.getName()), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/users/{id}/deleteUser")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") int userId, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), userId);
        User user = sessionManager.getSessionUser(request.getSession());
        userService.deleteUser(user);
        return new ResponseEntity<>("Your delete request was successful.", HttpStatus.ACCEPTED);
    }


}
