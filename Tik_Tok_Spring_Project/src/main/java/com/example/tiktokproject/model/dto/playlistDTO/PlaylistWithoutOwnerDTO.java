package com.example.tiktokproject.model.dto.playlistDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaylistWithoutOwnerDTO {

    private int id;
    private String name;
    private LocalDateTime createdTime;
    @JsonProperty("owner")
    private UserWithoutPostDTO userWithoutPost;
    @JsonProperty("posts")
    private List<PostWithoutOwnerDTO> postsWithoutOwner;

    public void addPost(PostWithoutOwnerDTO postDTO) {
        if(postsWithoutOwner == null){
            postsWithoutOwner = new ArrayList<>();
        }
        this.postsWithoutOwner.add(postDTO);
    }
}
