package com.example.tiktokproject.model.pojo;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Comment {

    private int id;
    private int owner_id;
    private int parent_id;
    private int post_id;
    private String text;
    private LocalDateTime commented_on;

}
