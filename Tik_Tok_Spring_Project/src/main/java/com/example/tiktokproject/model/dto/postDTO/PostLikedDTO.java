package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class PostLikedDTO {

    private int id;
    private int views;
    private String videoUrl;
}
