package com.example.tiktokproject.controller;

import com.example.tiktokproject.model.dto.hashtagDTO.HashtagResponseDTO;
import com.example.tiktokproject.model.repository.HashtagRepository;
import com.example.tiktokproject.services.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HashtagController {

    @Autowired
    HashtagService hashtagServicer;

    @GetMapping("hashtags/{title}")
    public ResponseEntity<HashtagResponseDTO> getAllPostsByHashtag(@PathVariable String title){
        return new ResponseEntity<>(hashtagServicer.getAllPostsByHashtag(title), HttpStatus.OK);
    }
}
