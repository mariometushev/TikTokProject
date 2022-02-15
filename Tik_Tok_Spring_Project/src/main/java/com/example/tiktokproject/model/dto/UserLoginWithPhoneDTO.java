package com.example.tiktokproject.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginWithPhoneDTO {

    private String phone;
    private String password;
}