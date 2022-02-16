package com.example.tiktokproject.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserRegisterResponseWithPhoneDTO {

    private int id;
    private String phoneNumber;
    private int role_id;

}
