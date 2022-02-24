package com.example.tiktokproject.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Comment parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Comment> replies = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @Column
    private String text;
    @Column(name = "commented_on")
    private LocalDateTime commentedOn;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "comments_have_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> commentLikes = new ArrayList<>();

    public void addUserWhoLike(User u) {
        this.commentLikes.add(u);
    }

    public void removeUserWhoLike(User userWhoWantToUnlike) {
        this.commentLikes.remove(userWhoWantToUnlike);
    }
}
