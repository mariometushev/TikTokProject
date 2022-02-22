package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String userEmail);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameLike(String search);

    @Query(value = "SELECT * FROM users AS u " +
            "WHERE u.last_login_attempt <= DATE_SUB(:date,INTERVAL 7 DAYS)  ", nativeQuery = true)
    List<User> findAllWhereLastLoginAttemptIsBefore(@Param("date") String localDateNow);

    @Query(value = "SELECT * FROM users AS u " +
            "JOIN followers AS f ON(u.id= f.followed_to_id) " +
            "WHERE u.username LIKE :search " +
            "GROUP BY f.followed_to_id " +
            "ORDER BY f.followed_to_id DESC LIMIT :limit ", nativeQuery = true)
    List<User> findBySearch(@Param("search") String search, @Param("limit") Integer limit);
}
