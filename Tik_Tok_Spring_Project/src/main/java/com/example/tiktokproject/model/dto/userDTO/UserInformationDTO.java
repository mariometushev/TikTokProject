package com.example.tiktokproject.model.dto.userDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserInformationDTO {

    private int id;
    private String username;
    private String name;
    private String description;
    @JsonProperty("user posts")
    private List<PostWithoutOwnerDTO> userInfoPosts = new ArrayList<>();
    private String photoUrl;
    private int numberOfFollowers;
    private int numberOfFollowerTo;

    public void addPost(PostWithoutOwnerDTO postDTO) {
        userInfoPosts.add(postDTO);
    }

}
