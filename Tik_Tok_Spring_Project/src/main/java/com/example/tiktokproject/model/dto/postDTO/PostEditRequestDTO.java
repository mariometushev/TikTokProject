package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class PostEditRequestDTO {

    @NotNull(message = "privacy field can't be null")
    private boolean privacy;
    @Size(max = 150, message = "description length should be maximum 150 symbols")
    private String description;
}
