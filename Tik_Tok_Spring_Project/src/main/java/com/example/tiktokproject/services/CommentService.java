package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.commentDTO.*;
import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.example.tiktokproject.model.pojo.Comment;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.CommentRepository;
import com.example.tiktokproject.model.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        Comment c = modelMapper.map(comment, Comment.class);

        setCommentOwnerCommentPostAndCommentUploadDate(c, commentOwner, p, LocalDateTime.now());
        commentRepository.save(c);

        CommentResponseDTO response = modelMapper.map(c, CommentResponseDTO.class);
        UserWithoutPostDTO user = modelMapper.map(commentOwner, UserWithoutPostDTO.class);
        PostWithoutOwnerDTO post = modelMapper.map(p, PostWithoutOwnerDTO.class);

        post.setPostHaveComments(p.getPostComments().size());
        post.setPostHaveLikes(p.getPostLikes().size());
        response.setUserWithoutPost(user);
        response.setPostWithoutOwner(post);
        response.setCommentHasLikes(c.getCommentLikes().size());
        return response;
    }

    @Transactional(rollbackForClassName = "SQLException.class")
    public CommentReplyResponseDTO replyComment(User commentOwner, int commentId, CommentRequestDTO replyComment) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not found comment"));
        Comment reply = modelMapper.map(replyComment, Comment.class);
        Post p = c.getPost();

        setCommentOwnerCommentPostAndCommentUploadDate(reply, commentOwner, p, LocalDateTime.now());
        reply.setParent(c);

        postRepository.save(p);
        commentRepository.save(c);
        commentRepository.save(reply);
        commentRepository.insertIntoTable(c.getId(), reply.getId());

        CommentWithoutOwnerDTO parent = modelMapper.map(c, CommentWithoutOwnerDTO.class);
        parent.setCommentHasLikes(c.getCommentLikes().size());
        parent.setCommentHasReplies(commentRepository.findRepliesByCommentId(c.getId()));

        CommentReplyResponseDTO response = modelMapper.map(reply, CommentReplyResponseDTO.class);
        UserWithoutPostDTO user = modelMapper.map(c.getOwner(), UserWithoutPostDTO.class);
        PostWithoutOwnerDTO post = modelMapper.map(p, PostWithoutOwnerDTO.class);

        post.setPostHaveComments(p.getPostComments().size());
        post.setPostHaveLikes(p.getPostLikes().size());
        response.setUserWithoutPost(user);
        response.setPostWithoutOwner(post);
        response.setCommentHasLikes(reply.getCommentLikes().size());
        response.setParent(parent);
        return response;
    }

    public CommentEditResponseDTO editComment(User commentOwner, int commentId, CommentRequestDTO comment) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No such comment"));
        if (!commentOwner.getComments().contains(c)) {
            throw new UnauthorizedException("You can't edit another user comment");
        }

        c.setText(comment.getText());
        c.setCommentedOn(LocalDateTime.now());
        commentRepository.save(c);

        CommentEditResponseDTO response = modelMapper.map(c, CommentEditResponseDTO.class);
        response.setCommentHasLikes(c.getCommentLikes().size());
        return response;
    }

    @Transactional(rollbackForClassName = "SQLException.class")
    public void deleteComment(User commentOwner, int commentId) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No such comment"));
        if (!commentOwner.getComments().contains(c)) {
            throw new UnauthorizedException("You can't delete another user comment");
        }
        List<Integer> replies = commentRepository.findAllCommentRepliesId(commentId);
        for (Integer i : replies) {
            commentRepository.deleteById(i);
        }
        commentRepository.delete(c);
    }

    public void likeComment(User liker, int commentId) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No such comment to like"));
        if (liker.getUserLikedComments().contains(c)) {
            throw new BadRequestException("You can't like that comment two times");
        }
        c.addUserWhoLike(liker);
        commentRepository.save(c);
    }

    public void unlikeComment(User userWhoWantToUnlike, int commentId) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("No such comment to unlike"));
        if (!userWhoWantToUnlike.getUserLikedComments().contains(c)) {
            throw new BadRequestException("You already unlike this comment");
        }
        c.removeUserWhoLike(userWhoWantToUnlike);
        commentRepository.save(c);
    }

    public CommentGetResponseDTO getCommentById(int commentId) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not found comment"));

        CommentGetResponseDTO commentDto = modelMapper.map(c, CommentGetResponseDTO.class);
        PostWithoutOwnerDTO post = modelMapper.map(c.getPost(), PostWithoutOwnerDTO.class);

        post.setPostHaveComments(c.getPost().getPostComments().size());
        post.setPostHaveLikes(c.getPost().getPostLikes().size());
        UserWithoutPostDTO user = modelMapper.map(c.getOwner(), UserWithoutPostDTO.class);
        commentDto.setPostWithoutOwner(post);
        commentDto.setUserWithoutPost(user);
        commentDto.setCommentHasLikes(c.getCommentLikes().size());
        return commentDto;
    }

    public List<CommentWithoutOwnerDTO> getAllCommentsByPostId(int postId, int pageNumber, int rowsNumber) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        Pageable page = PageRequest.of(pageNumber, rowsNumber, Sort.by("commentedOn").descending());
        List<Comment> comments = commentRepository.findAllByPost(post, page);
        List<CommentWithoutOwnerDTO> commentWithoutOwner = new ArrayList<>();
        for (Comment comment : comments) {
            CommentWithoutOwnerDTO commentDto = modelMapper.map(comment, CommentWithoutOwnerDTO.class);
            commentDto.setCommentHasReplies(commentRepository.findRepliesByCommentId(comment.getId()));
            commentDto.setCommentHasLikes(comment.getCommentLikes().size());
            commentWithoutOwner.add(commentDto);
        }
        return commentWithoutOwner;
    }

    private void setCommentOwnerCommentPostAndCommentUploadDate(Comment c, User u, Post p, LocalDateTime dateTime) {
        c.setOwner(u);
        c.setPost(p);
        c.setCommentedOn(dateTime);
    }
}
