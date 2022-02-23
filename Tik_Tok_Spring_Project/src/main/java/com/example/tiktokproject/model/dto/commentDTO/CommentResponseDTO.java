package com.example.tiktokproject.model.dto.commentDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDTO {

    private int id;
    @JsonProperty("comment owner")
    private UserWithoutPostDTO userWithoutPost;
    @JsonProperty("comment post")
    private PostWithoutOwnerDTO postWithoutOwner;
    private String text;
    private LocalDateTime commentedOn;
    @JsonProperty("comment likes")
    private int commentHasLikes;

}
