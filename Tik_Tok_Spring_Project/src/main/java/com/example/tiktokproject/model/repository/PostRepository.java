package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Hashtag;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findById(int id);

    List<Post> findPostsByPostLikesContains(User u, Pageable pageable);

    @Query(value = "SELECT * FROM posts AS p " +
            "JOIN users AS u ON (u.id = p.owner_id) " +
            "WHERE u.id = :id " +
            "ORDER BY p.upload_date DESC", nativeQuery = true)
    List<Post> findPostsByUploadDate(@Param("id") Integer id, Pageable pageable);

    List<Post> findAllBy(Hashtag hashtag, Pageable page);

}
