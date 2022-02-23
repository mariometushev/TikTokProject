package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class PostUploadRequestDTO {

    @NotNull(message = "field isPublic can't be null")
    private boolean privacy;
    @Size(max = 150, message = "description length should be maximum 255 symbols")
    private String description;
}
