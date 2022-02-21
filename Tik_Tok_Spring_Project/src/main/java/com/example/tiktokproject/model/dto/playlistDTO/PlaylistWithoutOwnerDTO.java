package com.example.tiktokproject.model.dto.playlistDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PlaylistWithoutOwnerDTO {

    private int id;
    private String name;
    private LocalDateTime createdTime;
    private List<PostWithoutOwnerDTO> postsWithoutOwner;

    public void addPost(PostWithoutOwnerDTO postDTO) {
        this.postsWithoutOwner.add(postDTO);
    }
}
