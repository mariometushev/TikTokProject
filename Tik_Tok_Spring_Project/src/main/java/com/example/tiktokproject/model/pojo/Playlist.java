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
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column
    private String name;
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    @ManyToMany
    @JoinTable(
            name = "posts_in_playlist",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private Set<Post> posts;

    public void addPost(Post post) {
        posts.add(post);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
    }
}
