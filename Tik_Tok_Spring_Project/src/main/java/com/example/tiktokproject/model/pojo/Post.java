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
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

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

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "posts_have_hashtags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags;

    @ManyToMany
    @JoinTable(
            name = "posts_in_playlist",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private Set<Playlist> playlists;

    @ManyToMany
    @JoinTable(
            name = "posts_have_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> postLikes;

    public void addLike(User user) {
        this.postLikes.add(user);
    }

    public void removeLike(User user) {
        this.postLikes.remove(user);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}

