package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostUploadResponseDTO {

    private int id;
    private int ownerId;
    private LocalDateTime uploadDate;
    private boolean isPublic;
    private String description;
}
