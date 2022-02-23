package com.example.tiktokproject.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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
    private boolean privacy;

    @Column
    private String description;

    @Column
    private int views;

    @Column(name = "video_url")
    private String videoUrl;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private Set<Comment> postComments = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "posts_have_hashtags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new HashSet<>();

    @ManyToMany(mappedBy = "posts")
    private Set<Playlist> playlists;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "posts_have_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> postLikes = new HashSet<>();

    public void addLike(User user) {
        this.postLikes.add(user);
    }

    public void removeLike(User user) {
        this.postLikes.remove(user);
    }

    public void addHashtag(Hashtag hash) {
        this.hashtags.add(hash);
    }

    public void addComment(Comment c) {
        this.postComments.add(c);
    }
}

