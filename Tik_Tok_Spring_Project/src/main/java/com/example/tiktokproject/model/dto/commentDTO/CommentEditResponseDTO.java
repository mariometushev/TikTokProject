package com.example.tiktokproject.model.dto.commentDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentEditResponseDTO {

    private int id;
    private String text;
    private LocalDateTime commentedOn;
    @JsonProperty("comment likes")
    private int commentHasLikes;

}
