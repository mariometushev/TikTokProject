package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class PostEditRequestDTO {

    private int id;
    private int ownerId;
    private boolean isPublic;
    @Max(150)
    private String description;
}
