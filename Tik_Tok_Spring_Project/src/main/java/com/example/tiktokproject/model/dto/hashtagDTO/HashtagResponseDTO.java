package com.example.tiktokproject.model.dto.hashtagDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithOwnerDTO;
import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.pojo.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class HashtagResponseDTO {

    private int id;
    private String title;
    @JsonProperty("posts")
    private Set<PostWithOwnerDTO> postsWithOwner = new HashSet<>();

    public void addPost(PostWithOwnerDTO postDto) {
        postsWithOwner.add(postDto);
    }
}
