package com.example.tiktokproject.model.pojo;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Post {

    private int id;
    private int owner_id;
    private int song_id;
    private LocalDateTime upload_date;
    private boolean is_public;
    private String description;
    private int views;
    private String video_url;

}
