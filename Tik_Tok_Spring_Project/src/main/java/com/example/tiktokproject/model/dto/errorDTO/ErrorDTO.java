package com.example.tiktokproject.model.dto.errorDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class ErrorDTO {

    private String msg;
    private int status;
}
