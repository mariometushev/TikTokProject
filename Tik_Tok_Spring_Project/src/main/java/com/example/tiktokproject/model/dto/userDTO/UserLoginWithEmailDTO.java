package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class UserLoginWithEmailDTO {

    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+.[a-zA-Z]+$")
    @Max(255)
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}")
    @Max(50)
    private String password;
}
