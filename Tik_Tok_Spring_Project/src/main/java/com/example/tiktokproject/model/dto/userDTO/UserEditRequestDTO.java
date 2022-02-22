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
public class UserEditRequestDTO {

    private int id;
    @Size(max = 50, message = "username length should be maximum 50 symbols")
    private String username;
    @Size(max = 50, message = "name length should be maximum 50 symbols")
    private String name;
    @Size(max = 255, message = "email length should be maximum 255 symbols")
    private String email;
    @Size(max = 45, message = "name length should be maximum 45 symbols")
    private String phoneNumber;
    @Size(max = 50, message = "password length should be maximum 50 symbols")
    private String password;
    @Size(max = 50, message = "new password length should be maximum 50 symbols")
    private String newPassword;
    private String confirmNewPassword;
    @Size(max = 150, message = "description length should be maximum 150 symbols")
    private String description;


}
