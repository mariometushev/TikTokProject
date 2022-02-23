package com.example.tiktokproject.model.dto.postDTO;

import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostWithOwnerDTO {

    private int id;
    @JsonProperty("owner")
    private UserWithoutPostDTO userWithoutPost;
    private String description;
    private String videoUrl;
    private LocalDateTime uploadDate;
    private int views;
    @JsonProperty("post likes")
    private int postHaveLikes;
    @JsonProperty("post comments")
    private int postHaveComments;
}
