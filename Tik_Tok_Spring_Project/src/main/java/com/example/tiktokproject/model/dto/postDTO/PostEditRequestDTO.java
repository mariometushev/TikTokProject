package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class PostEditRequestDTO {

    private int id;
    private int ownerId;
    private boolean isPublic;
    @Size(max = 150, message = "description length should be maximum 150 symbols")
    private String description;
}
