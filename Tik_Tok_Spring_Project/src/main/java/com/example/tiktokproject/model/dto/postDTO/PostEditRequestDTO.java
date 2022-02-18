package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostEditRequestDTO {

    private int id;
    private int ownerId;
    private boolean isPublic;
    private String description;
}
