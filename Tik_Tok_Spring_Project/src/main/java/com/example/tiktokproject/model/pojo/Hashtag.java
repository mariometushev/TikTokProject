package com.example.tiktokproject.model.pojo;

import com.example.tiktokproject.model.dto.postDTO.PostWithOwnerDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Component
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hashtags")
@EqualsAndHashCode
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @ManyToMany(mappedBy = "hashtags",fetch = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();

}
