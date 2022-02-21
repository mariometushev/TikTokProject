package com.example.tiktokproject.controller;

import com.example.tiktokproject.exceptions.MethodArgumentNotValidException;
import com.example.tiktokproject.model.dto.commentDTO.*;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.CommentRepository;
import com.example.tiktokproject.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class CommentController {

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{pId}/comment")
    public ResponseEntity<CommentResponseDTO> comment(@PathVariable(name = "pId") int postId,
                                                      HttpServletRequest request,
                                                      @Valid @RequestBody CommentRequestDTO comment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException("Wrong credentials");
        }
        sessionManager.validateLogin(request);
        User commentOwner = sessionManager.getSessionUser(request.getSession());
        return new ResponseEntity<>(commentService.makeComment(commentOwner, postId, comment), HttpStatus.CREATED);
    }

    @PostMapping("/comment/{cId}")
    public ResponseEntity<CommentReplyResponseDTO> replyComment(@PathVariable(name = "cId") int commentId,
                                                                @Valid @RequestBody CommentRequestDTO replyComment,
                                                                HttpServletRequest request,
                                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException("Wrong credentials");
        }
        sessionManager.validateLogin(request);
        User commentReplyOwner = sessionManager.getSessionUser(request.getSession());
        return new ResponseEntity<>(commentService.replyComment(commentReplyOwner, commentId, replyComment), HttpStatus.ACCEPTED);
    }


    @PutMapping("/comments/{cId}/edit")
    public ResponseEntity<CommentEditResponseDTO> editComment(@PathVariable(name = "cId") int commentId,
                                                              HttpServletRequest request,
                                                              @Valid @RequestBody CommentRequestDTO comment,
                                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException("Wrong credentials");
        }
        sessionManager.validateLogin(request);
        User commentOwner = sessionManager.getSessionUser(request.getSession());
        CommentEditResponseDTO response = commentService.editComment(commentOwner, commentId, comment);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/comments/{cId}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "cId") int commentId,
                                                HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User commentOwner = sessionManager.getSessionUser(request.getSession());
        commentService.deleteComment(commentOwner, commentId);
        return new ResponseEntity<>("Delete comment request was successful", HttpStatus.ACCEPTED);
    }

    @PostMapping("/comments/{cId}/like")
    public ResponseEntity<String> likeComment(@PathVariable(name = "cId") int commentId,
                                              HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User liker = sessionManager.getSessionUser(request.getSession());
        commentService.likeComment(liker, commentId);
        return new ResponseEntity<>("You like the comment successful", HttpStatus.ACCEPTED);
    }

    @PostMapping("/comments/{cId}/unlike")
    public ResponseEntity<String> unlikeComment(@PathVariable(name = "cId") int commentId,
                                                HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User userWhoWantToUnlike = sessionManager.getSessionUser(request.getSession());
        commentService.unlikeComment(userWhoWantToUnlike, commentId);
        return new ResponseEntity<>("You unlike the comment successful", HttpStatus.ACCEPTED);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentGetResponseDTO> getCommentById(@PathVariable(name = "id") int commentId, HttpServletRequest request){
        sessionManager.validateLogin(request);
        return new ResponseEntity<>(commentService.getCommentById(commentId), HttpStatus.OK);
    }

}
