package com.example.tiktokproject.model.dto.commentDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class CommentRequestDTO {

    @NotBlank
    @Min(1)
    @Max(150)
    private String text;

}
