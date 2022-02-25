package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Hashtag;
import com.example.tiktokproject.model.pojo.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {

    Optional<Hashtag> findHashtagByTitle(String title);

}
