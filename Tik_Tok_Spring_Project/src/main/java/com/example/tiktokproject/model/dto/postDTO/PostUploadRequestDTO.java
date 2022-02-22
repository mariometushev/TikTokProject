package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class PostUploadRequestDTO {

    private int ownerId;
    private boolean isPublic;
    @Size(max = 150, message = "description length should be maximum 255 symbols")
    private String description;
}
