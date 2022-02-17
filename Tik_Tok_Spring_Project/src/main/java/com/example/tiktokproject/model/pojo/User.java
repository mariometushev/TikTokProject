package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "role_id")
    private int roleId;
    @Column
    private String username;
    @Column
    private String name;
    @Column
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+.[a-zA-Z]+$",message = "Invalid email address")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}",message = "Weak password")
    private String password;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column
    private String description;
    @Column(name = "register_date")
    private LocalDateTime registerDate;
    @Column(name = "photo_url")
    private String photoUrl;

}

