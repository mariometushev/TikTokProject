package com.example.tiktokproject.model.dto.playlistDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class PlaylistRequestDTO {

    @NotBlank(message = "playlist name can't be blank")
    @Size(min = 1,max = 45,message = "playlist name should be at least 1 symbol and maximum 150 symbols")
    private String name;

}
