package com.example.tiktokproject.model.dto.playlistDTO;

import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class PlaylistResponseDTO {

    private int id;
    @JsonProperty("owner")
    private UserWithoutPostDTO user;
    private String name;
    private LocalDateTime createdTime;

}
