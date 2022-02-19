package com.example.tiktokproject.model.dto.postDTO;

import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostWithOwnerDTO {

    private int id;
    private UserWithoutPostDTO owner;
    private String description;
    private String videoUrl;
    private LocalDateTime uploadDate;
    private int likes;
    private int comments;
}
