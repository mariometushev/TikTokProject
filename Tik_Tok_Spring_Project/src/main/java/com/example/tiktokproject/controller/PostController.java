package com.example.tiktokproject.controller;

import com.example.tiktokproject.model.dto.postDTO.*;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.services.PostService;
import com.example.tiktokproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private UserService userService;

    @PostMapping("/users/{id}/makePost")
    public ResponseEntity<PostUploadResponseDTO> makePost(@PathVariable int id, @Valid @RequestBody PostUploadRequestDTO post,
                                                          HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), id);
        User user = sessionManager.getSessionUser(request.getSession());
        userService.changeUserRole(user);
        return new ResponseEntity<>(postService.makePost(post,user), HttpStatus.CREATED);
    }

    @PostMapping("/users/{uId}/uploadPostVideo/{pId}")
    public ResponseEntity<PostUploadResponseDTO> uploadPostVideo(@PathVariable int uId, @PathVariable int pId, @RequestParam(name = "file") MultipartFile file, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), uId);
        return new ResponseEntity<>(postService.uploadPostVideo(pId, file), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") int postId, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        postService.deletePost(postId, request.getSession());
        return new ResponseEntity<>("Successful delete request", HttpStatus.ACCEPTED);
    }

    @PutMapping("/posts/{id}/editPost")
    public ResponseEntity<PostEditResponseDTO> editPost(@PathVariable int id,@Valid @RequestBody PostEditRequestDTO postDto,
                                                        HttpServletRequest request) {
        sessionManager.validateLogin(request);
        return new ResponseEntity<>(postService.editPost(id,postDto, request.getSession()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostWithOwnerDTO> getById(@PathVariable int id) {
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<PostWithOwnerDTO>> getAllPostsSortByUploadDate(@PathVariable int id) {
        return new ResponseEntity<>(postService.getAllPostsSortByUploadDate(id), HttpStatus.OK);
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable int id, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User user = sessionManager.getSessionUser(request.getSession());
        postService.likePost(id, user);
        return new ResponseEntity<>("Your like request was successful", HttpStatus.ACCEPTED);
    }

    @PostMapping("/posts/{id}/unlike")
    public ResponseEntity<String> unlikePost(@PathVariable int id, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User user = sessionManager.getSessionUser(request.getSession());
        postService.unlikePost(id, user);
        return new ResponseEntity<>("Your unlike request was successful", HttpStatus.ACCEPTED);
    }
}
