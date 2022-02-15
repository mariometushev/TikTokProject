package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Getter
@Setter
@NoArgsConstructor
public class User {

    private int id;
    private int role_id;
    private String username;
    private String name;
    private String email;
    private String phone_number;
    private String password;
    private LocalDate date_of_birth;
    private String description;
    private LocalDateTime register_date;
    private String photo_url;

}
