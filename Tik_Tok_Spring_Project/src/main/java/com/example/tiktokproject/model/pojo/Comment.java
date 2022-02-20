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
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column(name = "parent_id")
    private int parentId;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @Column
    private String text;
    @Column(name = "commented_on")
    private LocalDateTime commentedOn;
    @ManyToMany
    @JoinTable(
            name = "comments_have_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> commentLikes;

    public void addUserWhoLike(User u) {
        this.commentLikes.add(u);
    }

    public void removeUserWhoLike(User userWhoWantToUnlike) {
        this.commentLikes.remove(userWhoWantToUnlike);
    }
}
