package com.example.tiktokproject.model.dto.commentDTO;

import com.example.tiktokproject.model.pojo.Comment;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@Getter
@Setter
public class CommentReplyResponseDTO {

    private int id;
    private User owner;
    private Comment parent;
    private Post post;
    private String text;
    private LocalDateTime commentedOn;
    private int likes;
}
