package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findById(int id);
    List<Post> findAllByOwner(User owner);
}
