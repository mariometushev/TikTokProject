package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
@Validated
public class UserForgottenPasswordDTO {

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}")
    @Max(50)
    private String newPassword;
    private String confirmNewPassword;
}
