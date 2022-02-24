package com.example.tiktokproject.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class UserSetUsernameDTO {

    @NotBlank(message = "username can't be blank")
    @NotNull(message = "username is mandatory")
    @Size(max = 50, message = "username length should be maximum 50 symbols")
    private String username;
    @Size(max = 50, message = "name length should be maximum 50 symbols")
    private String name;
}
