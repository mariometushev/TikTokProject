package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
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
    @Column
    private int role_id;
    @Column
    private String username;
    @Column
    private String name;
    @Column
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column
    private String password;
    @Column
    private LocalDate date_of_birth;
    @Column
    private String description;
    @Column
    private LocalDateTime register_date;
    @Column
    private String photo_url;

}

