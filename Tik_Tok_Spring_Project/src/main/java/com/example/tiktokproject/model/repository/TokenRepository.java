package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> getByToken(String token);
}
