package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterRequestWithPhoneDTO {

    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String password;
    private String confirmPassword;

}
