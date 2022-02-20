package com.example.tiktokproject.model.dto.userDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class UserEditRequestDTO {

    private int id;
    private int roleId;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String newPassword;
    private String confirmNewPassword;
    private String description;


}
