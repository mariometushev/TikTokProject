package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String userEmail);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameLike(String search);

    List<User> findAllWhereLastLoginAttemptIsBefore(LocalDateTime lastLoginAttempt);

}
