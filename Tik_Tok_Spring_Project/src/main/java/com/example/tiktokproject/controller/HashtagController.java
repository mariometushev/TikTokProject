package com.example.tiktokproject.controller;

import com.example.tiktokproject.model.dto.hashtagDTO.HashtagResponseDTO;
import com.example.tiktokproject.model.repository.HashtagRepository;
import com.example.tiktokproject.services.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HashtagController {

    @Autowired
    HashtagService hashtagService;

    @GetMapping("/hashtags")
    public ResponseEntity<HashtagResponseDTO> getAllPostsByHashtag(@RequestParam(name = "title") String title,
                                                                   @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                                   @RequestParam(name = "rowsNumber", defaultValue = "10") int rowsNumber) {
        return new ResponseEntity<>(hashtagService.getAllPostsByHashtag(title,pageNumber,rowsNumber), HttpStatus.OK);
    }
}
