package com.example.tiktokproject.model.dto.userDTO;


import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
    private LocalDate dateOfBirth;
    private String description;
    private String photoUrl;

}
