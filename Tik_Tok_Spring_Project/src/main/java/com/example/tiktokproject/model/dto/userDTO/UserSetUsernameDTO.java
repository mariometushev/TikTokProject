package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class UserSetUsernameDTO {

    private int id;
    @NotBlank
    @NotEmpty
    @NotNull
    private String username;
    @NotBlank
    private String name;
}
