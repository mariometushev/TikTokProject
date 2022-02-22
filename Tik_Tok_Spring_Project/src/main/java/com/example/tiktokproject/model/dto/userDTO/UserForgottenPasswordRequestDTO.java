package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class UserForgottenPasswordRequestDTO {

    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+.[a-zA-Z]+$", message = "Wrong email format")
    @Size(max = 255, message = "email length should be maximum 255 symbols")
    private String email;
}
