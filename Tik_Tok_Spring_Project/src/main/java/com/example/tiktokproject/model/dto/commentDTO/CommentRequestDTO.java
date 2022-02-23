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

    @NotBlank(message = "comment can't be null or blank")
    @Size(min = 1,max = 150,message = "comment must be at least 1 symbol and maximum 150 symbols")
    private String text;

}
