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
public class UserEditRequestDTO {

    private int id;
    @Max(50)
    private String username;
    @Max(50)
    private String name;
    @Max(255)
    private String email;
    @Max(45)
    private String phoneNumber;
    @Max(50)
    private String password;
    @Max(50)
    private String newPassword;
    private String confirmNewPassword;
    @Max(150)
    private String description;


}
