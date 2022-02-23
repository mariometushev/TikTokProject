package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Comment;
import com.example.tiktokproject.model.pojo.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO comments_have_comments (parent_id,child_id) VALUES (:parentId,:childId)", nativeQuery = true)
    void insertIntoTable(@Param("parentId") Integer parentId, @Param("childId") Integer childId);

    @Query(value = "SELECT COUNT(*) FROM comments_have_comments WHERE parent_id = :id",nativeQuery = true)
    int findRepliesByCommentId(@Param("id") Integer id);

    List<Comment> findAllByPost(Post post);
}
