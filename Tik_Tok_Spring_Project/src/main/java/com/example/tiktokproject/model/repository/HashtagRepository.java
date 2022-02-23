package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {

    Optional<Hashtag> findHashtagByTitle(String title);

}
