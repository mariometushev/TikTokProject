package com.example.tiktokproject.model.dto.userDTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class UserEditResponseDTO {

    private int id;
    private int roleId;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String description;
    private String photoUrl;

}
