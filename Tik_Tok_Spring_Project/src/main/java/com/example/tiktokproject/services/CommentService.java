package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.commentDTO.CommentEditResponseDTO;
import com.example.tiktokproject.model.dto.commentDTO.CommentRequestDTO;
import com.example.tiktokproject.model.dto.commentDTO.CommentResponseDTO;
import com.example.tiktokproject.model.pojo.Comment;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.CommentRepository;
import com.example.tiktokproject.model.repository.PostRepository;
import com.example.tiktokproject.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    public CommentResponseDTO makeComment(User commentOwner, int postId, CommentRequestDTO comment) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No such post to comment"));
        if (comment.getText().isBlank()) {
            throw new BadRequestException("Comment can't be blank");
        }
        if (comment.getText().length() > 150) {
            throw new BadRequestException("Maximum comment length is 150 symbols");
        }
        Comment c = modelMapper.map(comment, Comment.class);
        c.setPost(p);
        c.setText(comment.getText());
        c.setOwner(commentOwner);
        c.setCommentedOn(LocalDateTime.now());
        commentRepository.save(c);

        commentOwner.addComment(c);
        p.addComment(c);

        userRepository.save(commentOwner);
        postRepository.save(p);

        CommentResponseDTO response = modelMapper.map(c, CommentResponseDTO.class);
        response.setLikes(c.getCommentLikes().size());
        return response;
    }

    public CommentEditResponseDTO editComment(User commentOwner, int postId, int commentId, CommentRequestDTO comment) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No such comment"));
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No such post"));
        if (!p.getComments().contains(c)) {
            throw new BadRequestException("This post doesn't have that comment");
        }
        if (!commentOwner.getComments().contains(c)) {
            throw new UnauthorizedException("You can't edit another user comment");
        }
        if (comment.getText().isBlank()) {
            throw new BadRequestException("Comment can't be blank");
        }
        if (comment.getText().length() > 150) {
            throw new BadRequestException("Maximum comment length is 150 symbols");
        }

        c.setText(comment.getText());
        c.setCommentedOn(LocalDateTime.now());
        commentRepository.save(c);

        CommentEditResponseDTO response = modelMapper.map(c, CommentEditResponseDTO.class);
        response.setLikes(c.getCommentLikes().size());
        return response;
    }

    public void deleteComment(User commentOwner, int postId, int commentId) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No such post"));
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No such comment"));
        if (!p.getComments().contains(c)) {
            throw new BadRequestException("This post doesn't have that comment");
        }
        if (!commentOwner.getComments().contains(c)) {
            throw new UnauthorizedException("You can't delete another user comment");
        }

        commentOwner.removeComment(c);
        p.removeComment(c);

        commentRepository.delete(c);
        userRepository.save(commentOwner);
        postRepository.save(p);
    }

    public void likeComment(User liker, int postId, int commentId) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No such post"));
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No such comment to like"));
        if (!p.getComments().contains(c)) {
            throw new BadRequestException("This post doesn't have that comment");
        }
        if (liker.getUserLikedComments().contains(c)) {
            throw new BadRequestException("You can't like that comment two times");
        }

        liker.addLikedComment(c);
        c.addUserWhoLike(liker);

        commentRepository.save(c);
        userRepository.save(liker);
    }

    public void unlikeComment(User userWhoWantToUnlike, int postId, int commentId) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No such comment to unlike"));
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No such post"));
        if(!p.getComments().contains(c)){
            throw new BadRequestException("This post doesn't have that comment");
        }
        if(!userWhoWantToUnlike.getUserLikedComments().contains(c)){
            throw new BadRequestException("You already unlike this comment");
        }

        userWhoWantToUnlike.removeLikedComment(c);
        c.removeUserWhoLike(userWhoWantToUnlike);

        commentRepository.save(c);
        userRepository.save(userWhoWantToUnlike);
    }
}
