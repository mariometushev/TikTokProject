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

    @OneToMany(mappedBy = "owner")
    private Set<Playlist> playlists;

    @OneToMany(mappedBy = "owner")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "owner")
    private Set<Post> posts;

    @ManyToMany(mappedBy = "postLikes")
    private Set<Post> userLikedPosts;

    @ManyToMany(mappedBy = "commentLikes")
    private Set<Comment> userLikedComments;

    @ManyToMany
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "followed_to_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers;

    @ManyToMany(mappedBy = "followers")
    private Set<User> followerTo;

    public void addFollower(User follower) {
        this.getFollowers().add(follower);
    }

    public void removeFollower(User userWhoWantToUnfollow) {
        this.getFollowers().remove(userWhoWantToUnfollow);
    }

    public void addComment(Comment c) {
        this.comments.add(c);
    }

    public void addLikedComment(Comment c) {
        this.userLikedComments.add(c);
    }

    public void removeComment(Comment c) {
        this.comments.remove(c);
    }

    public void addLikedPost(Post post) {
        this.userLikedPosts.add(post);
    }

    public void removeLikedPost(Post post) {
        this.userLikedPosts.remove(post);
    }

    public void removeLikedComment(Comment c) {
        this.userLikedComments.remove(c);
    }
}

