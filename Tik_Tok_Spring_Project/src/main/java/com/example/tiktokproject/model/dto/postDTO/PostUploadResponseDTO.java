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
public class PostUploadResponseDTO {

    private int id;
    @JsonProperty("owner")
    private UserWithoutPostDTO ownerWithoutPost;
    private LocalDateTime uploadDate;
    private String description;
    @JsonProperty("is public")
    private boolean privacy;

}
