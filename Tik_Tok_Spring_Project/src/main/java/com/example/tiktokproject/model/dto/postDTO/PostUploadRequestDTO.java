package com.example.tiktokproject.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class PostUploadRequestDTO {

    private int ownerId;
    private boolean isPublic;
    @Max(150)
    private String description;
}
