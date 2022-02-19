package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostUploadRequestDTO {

    private int ownerId;
    private boolean isPublic;
    private String description;
}