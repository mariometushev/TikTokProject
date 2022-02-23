package com.example.tiktokproject.model.dto.commentDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class CommentWithoutOwnerDTO {

    private int id;
    private String text;
    private LocalDateTime commentedOn;
    @JsonProperty("comment replies")
    private int commentHasReplies;
    @JsonProperty("comment likes")
    private int commentHasLikes;

}
