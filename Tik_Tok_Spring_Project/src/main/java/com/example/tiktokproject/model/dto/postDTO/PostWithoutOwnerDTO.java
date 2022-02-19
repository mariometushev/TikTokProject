package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostWithoutOwnerDTO {

    private int id;
//    private String songUrl;
    private LocalDateTime uploadDate;
    private String description;
    private int views;
    private String videoUrl;
    private int comments;
    private int postLikes;

}
