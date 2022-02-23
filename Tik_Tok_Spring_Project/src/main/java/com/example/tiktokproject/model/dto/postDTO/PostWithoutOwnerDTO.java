package com.example.tiktokproject.model.dto.postDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostWithoutOwnerDTO {

    private int id;
    private LocalDateTime uploadDate;
    private String description;
    private int views;
    private String videoUrl;
    @JsonProperty("post comments")
    private int postHaveComments;
    @JsonProperty("post likes")
    private int postHaveLikes;

}
