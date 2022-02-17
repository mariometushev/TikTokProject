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
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "owner_id")
    private int ownerId;
    @Column(name = "parent_id")
    private int parentId;
    @Column(name = "post_id")
    private int postId;
    @Column
    private String text;
    @Column(name = "commented_on")
    private LocalDateTime commentedOn;

}
