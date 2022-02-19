package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
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

    public CommentResponseDTO makeComment(User commentOwner, int postId, CommentRequestDTO comment) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("No such post to comment"));
        if (comment.getCommentText().isBlank()) {
            throw new BadRequestException("Comment can't be blank");
        }
        if (comment.getCommentText().length() > 150) {
            throw new BadRequestException("Maximum comment length is 150 symbols");
        }
        Comment c = modelMapper.map(comment, Comment.class);
        c.setPost(p);
        c.setText(comment.getCommentText());
        c.setOwner(commentOwner);
        c.setCommentedOn(LocalDateTime.now());
        commentRepository.save(c);
        return modelMapper.map(c, CommentResponseDTO.class);
    }

}
