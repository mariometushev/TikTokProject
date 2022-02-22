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

    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+.[a-zA-Z]+$",message = "wrong email format")
    @Size(max = 255, message = "email length should be maximum 255 symbols")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}",
            message = "password should contain one special symbol, one digit, " +
                    "one capital && small letter and minimum size length should be 8 symbols")
    @Size(max = 50, message = "password length should be maximum 50 symbols")
    private String password;
}
