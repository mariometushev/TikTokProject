package com.example.tiktokproject.model.dto.playlistDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class PlaylistRequestDTO {

    @NotBlank
    @Min(1)
    @Max(45)
    private String name;

}
