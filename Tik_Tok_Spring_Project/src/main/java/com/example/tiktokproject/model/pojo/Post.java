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
    @Column(name = "owner_id")
    private int ownerId;
    @Column(name = "song_id")
    private int songId;
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    @Column(name = "is_public")
    private boolean isPublic;
    @Column
    private String description;
    @Column
    private int views;
    @Column(name = "video_url")
    private String videoUrl;

}
