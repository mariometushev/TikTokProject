package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByPhone_number(String phone);

    Optional<User> findPasswordByEmail(String email);

    Optional<User> findPasswordByPhone_number(String phone);

    Optional<User> findByEmail(String userEmail);

}
