package com.example.tiktokproject.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserRegisterRequestWithPhoneDTO {

    private LocalDate date_of_birth;
    private String phoneNumber;
    private String password;
    private String confirmPassword;

}
