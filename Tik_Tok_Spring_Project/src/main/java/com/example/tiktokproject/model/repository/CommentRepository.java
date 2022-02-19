package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
