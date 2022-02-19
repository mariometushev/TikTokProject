package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByPhoneNumber(String phone_number);

    Optional<User> findByEmail(String userEmail);

    Optional<User> findByUsername(String username);

}
