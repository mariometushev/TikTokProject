package com.example.tiktokproject.controller;

import com.example.tiktokproject.model.dto.commentDTO.CommentEditResponseDTO;
import com.example.tiktokproject.model.dto.commentDTO.CommentRequestDTO;
import com.example.tiktokproject.model.dto.commentDTO.CommentResponseDTO;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.CommentRepository;
import com.example.tiktokproject.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @PostMapping("users/{uId}/posts/{pId}")//TODO
    public ResponseEntity<CommentResponseDTO> comment(@PathVariable(name = "uId") int userId,
                                                      @PathVariable(name = "pId") int postId,
                                                      HttpServletRequest request,
                                                      @RequestBody CommentRequestDTO comment) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(),userId);
        User commentOwner = sessionManager.getSessionUser(request.getSession());
        CommentResponseDTO response = commentService.makeComment(commentOwner,postId,comment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/posts/{pId}/comments/{cId}")
    public ResponseEntity<CommentEditResponseDTO> editComment(@PathVariable(name = "cId") int commentId,
                                                              @PathVariable(name = "pId") int postId,
                                                              HttpServletRequest request,
                                                              @RequestBody CommentRequestDTO comment) {
        sessionManager.validateLogin(request);
        User commentOwner = sessionManager.getSessionUser(request.getSession());
        CommentEditResponseDTO response = commentService.editComment(commentOwner,postId,commentId,comment);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


}
