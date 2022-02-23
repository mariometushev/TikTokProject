package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column
    private String name;
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "posts_in_playlist",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> posts = new HashSet<>();

    public void addPost(Post post) {
        if (posts == null) {
            posts = new HashSet<>();
        }
        posts.add(post);
    }

    public void removePost(Post post) {
        posts.remove(post);
    }
}
