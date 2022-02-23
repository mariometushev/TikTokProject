package com.example.tiktokproject.model.dto.postDTO;

import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostEditResponseDTO {

    private int id;
    @JsonProperty("owner")
    private UserWithoutPostDTO userWithoutPost;
    private LocalDateTime uploadDate;
    @JsonProperty("is public")
    private boolean privacy;
    private String description;
    private int views;
    private String videoUrl;
}
