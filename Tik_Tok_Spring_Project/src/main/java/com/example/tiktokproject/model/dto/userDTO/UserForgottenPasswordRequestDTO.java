package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class UserForgottenPasswordRequestDTO {

    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+.[a-zA-Z]+$")
    @Max(255)
    private String email;
}
