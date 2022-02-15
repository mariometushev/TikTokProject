package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    String findByEmail(String userEmail);

}
