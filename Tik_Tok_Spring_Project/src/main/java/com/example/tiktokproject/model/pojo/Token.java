package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Component
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users_have_tokens")
public class Token {

    public static final int EXPIRATION = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column
    private String token;
    @Column(name = "expity_date")
    private LocalDateTime expiryDate;
}
