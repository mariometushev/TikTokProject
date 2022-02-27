package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Comment;
import com.example.tiktokproject.model.pojo.Post;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT COUNT(*) FROM comments_have_comments WHERE parent_id = :id", nativeQuery = true)
    int findRepliesByCommentId(@Param("id") Integer id);

    List<Comment> findAllByPost(Post post, Pageable pageable);

    @Query(value = "SELECT child_id FROM comments_have_comments WHERE parent_id = :commentId", nativeQuery = true)
    List<Integer> findAllCommentRepliesId(@Param("commentId") Integer commentId);

    @Query(value = "SELECT * FROM comments AS c JOIN comments_have_comments as chc ON(c.id = chc.child_id)" +
            " WHERE chc.parent_id = :id", nativeQuery = true)
    List<Comment> findCommentRepliesById(@Param("id") Integer id, Pageable pageable);
}
