package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserLoginResponseWithEmailDTO {

    private int id;
    private String email;

}
