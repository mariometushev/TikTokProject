package com.example.tiktokproject.model.dto.commentDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentReplyDTO {

    private int id;
    @JsonProperty("comment owner")
    private UserWithoutPostDTO userWithoutPost;
    private String text;
    private LocalDateTime commentedOn;
    @JsonProperty("comment likes")
    private int commentHasLikes;
}
