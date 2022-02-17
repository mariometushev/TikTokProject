package com.example.tiktokproject.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserRegisterResponseWithEmailDTO {

    private int id;
    private String email;
    private int roleId;

}
