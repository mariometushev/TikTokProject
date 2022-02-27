package com.example.tiktokproject.model.dto.commentDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CommentWithRepliesDTO {

    private int id;
    @JsonProperty("comment owner")
    private UserWithoutPostDTO userWithoutPost;
    @JsonProperty("comment post")
    private PostWithoutOwnerDTO postWithoutOwner;
    private List<CommentReplyDTO> commentReplies;
    private String text;
    private LocalDateTime commentedOn;
    private int commentHasLikes;
}
