package com.example.tiktokproject.controller;

import com.example.tiktokproject.model.dto.postDTO.*;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("users/{id}/uploadPost")
    public ResponseEntity<PostUploadResponseDTO> uploadPost(@PathVariable int id,@RequestBody PostUploadRequestDTO post, @RequestParam(name = "file") MultipartFile file, HttpServletRequest request){
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(),id);
        return new ResponseEntity<>(postService.uploadPost(post, file), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public HttpStatus deletePost(@PathVariable(name = "id") int postId, HttpServletRequest request){
        sessionManager.validateLogin(request);
        postService.deletePost(postId, request.getSession());
        return HttpStatus.ACCEPTED;
    }

    @PutMapping("/posts/editPost")
    public ResponseEntity<PostEditResponseDTO> editPost(@RequestBody PostEditRequestDTO postDto, HttpServletRequest request){
        sessionManager.validateLogin(request);
        PostEditResponseDTO dto = postService.editPost(postDto, request.getSession());
        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostWithOwnerDTO> getById(@PathVariable int id){
        PostWithOwnerDTO postDto = postService.getPost(id);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<Post>> getAllPostsSortByUploadDate(@PathVariable int id){
            return new ResponseEntity<>(postService.getAllPostsSortByUploadDate(id), HttpStatus.OK);
    }
}
