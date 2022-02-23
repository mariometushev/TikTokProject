package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "role_id")
    private int roleId;
    @Column
    private String username;
    @Column
    private String name;
    @Column
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column
    private String password;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column
    private String description;
    @Column(name = "register_date")
    private LocalDateTime registerDate;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "is_verified")
    private boolean isVerified;
    @Column(name = "last_login_attempt")
    private LocalDateTime lastLoginAttempt;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Playlist> playlists = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(mappedBy = "postLikes", fetch = FetchType.LAZY)
    private List<Post> userLikedPosts = new ArrayList<>();

    @ManyToMany(mappedBy = "commentLikes", fetch = FetchType.LAZY)
    private List<Comment> userLikedComments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "followed_to_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followers = new ArrayList<>();

    @ManyToMany(mappedBy = "followers", fetch = FetchType.LAZY)
    private List<User> followerTo = new ArrayList<>();

    public void addFollower(User follower) {
        this.getFollowers().add(follower);
    }

    public void removeFollower(User userWhoWantToUnfollow) {
        this.getFollowers().remove(userWhoWantToUnfollow);
    }

    public void addComment(Comment c) {
        this.comments.add(c);
    }

}

