package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Component
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "owner_id")
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column
    private String name;
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    @ManyToMany(mappedBy = "playlists")
    private Set<Post> posts;

}
