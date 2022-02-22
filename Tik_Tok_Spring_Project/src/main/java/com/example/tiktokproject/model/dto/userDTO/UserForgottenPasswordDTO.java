package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class UserForgottenPasswordDTO {

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}",
            message = "password should contain one special symbol, one digit, " +
                    "one capital && small letter and minimum size length should be 8 symbols")
    @Size(max = 50, message = "new password length should be maximum 50 symbols")
    private String newPassword;
    private String confirmNewPassword;
}
