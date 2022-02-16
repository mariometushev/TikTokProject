package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Component
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int owner_id;
    @Column
    private int song_id;
    @Column
    private LocalDateTime upload_date;
    @Column
    private boolean is_public;
    @Column
    private String description;
    @Column
    private int views;
    @Column
    private String video_url;

}
