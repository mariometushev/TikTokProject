package com.example.tiktokproject.model.dto.hashtagDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithOwnerDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class HashtagResponseDTO {

    private int id;
    private String title;
    @JsonProperty("posts")
    private List<PostWithOwnerDTO> postsWithOwner = new ArrayList<>();

    public void addPost(PostWithOwnerDTO postDto) {
        postsWithOwner.add(postDto);
    }
}
