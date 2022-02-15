package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

     User findByPhone_number(String phone);
     User findByEmail(String email);
     String findPasswordByEmail(String email);
     String findPasswordByPhone_number(String phone);



}
