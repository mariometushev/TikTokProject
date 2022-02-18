package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostEditResponseDTO {

    private int id;
    private int ownerId;
    private int songId;
    private LocalDateTime uploadDate;
    private boolean isPublic;
    private String description;
    private int views;
    private String videoUrl;
}
