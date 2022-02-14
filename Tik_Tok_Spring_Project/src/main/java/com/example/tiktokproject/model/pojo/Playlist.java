package com.example.tiktokproject.model.pojo;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Playlist {

    private int id;
    private int owner_id;
    private String name;
    private LocalDateTime created_time;


}
