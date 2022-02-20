package com.example.tiktokproject.model.dto.userDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserInformationDTO {

    private int id;
    private String username;
    private String name;
    private String description;
    private Set<PostWithoutOwnerDTO> posts;
    private String photoUrl;
    private int numberOfFollowers;
    private int numberOfFollowerTo;

    public void addPost(PostWithoutOwnerDTO postDTO) {
        posts.add(postDTO);
    }

}
