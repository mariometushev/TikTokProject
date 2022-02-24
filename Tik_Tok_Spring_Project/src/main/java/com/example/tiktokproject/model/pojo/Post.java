package com.example.tiktokproject.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> postComments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "posts_have_hashtags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags = new ArrayList<>();

    @ManyToMany(mappedBy = "posts", fetch = FetchType.LAZY)
    private List<Playlist> playlists = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "posts_have_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> postLikes = new ArrayList<>();

    public void addLike(User user) {
        this.postLikes.add(user);
    }

    public void removeLike(User user) {
        this.postLikes.remove(user);
    }

    public void addHashtag(Hashtag hash) {
        this.hashtags.add(hash);
    }

    public void removeHashtag(Hashtag hash) {
        this.hashtags.remove(hash);
    }
}

