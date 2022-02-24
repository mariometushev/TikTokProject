package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.model.dto.hashtagDTO.HashtagResponseDTO;
import com.example.tiktokproject.model.dto.postDTO.PostWithOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.example.tiktokproject.model.pojo.Hashtag;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.repository.HashtagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagService {

    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private ModelMapper modelMapper;

    public HashtagResponseDTO getAllPostsByHashtag(String title) {
        Hashtag hashtag = hashtagRepository.findHashtagByTitle(title).orElseThrow(() -> new NotFoundException("Hashtag not found"));
        HashtagResponseDTO hashtagDto = modelMapper.map(hashtag, HashtagResponseDTO.class);
        List<Post> hashtagPosts = hashtag.getPosts();
        for (Post post : hashtagPosts) {
            PostWithOwnerDTO postDto = modelMapper.map(post, PostWithOwnerDTO.class);
            postDto.setPostHaveComments(post.getPostComments().size());
            postDto.setPostHaveLikes(post.getPostLikes().size());
            UserWithoutPostDTO userDto = modelMapper.map(post.getOwner(), UserWithoutPostDTO.class);
            postDto.setUserWithoutPost(userDto);
            hashtagDto.addPost(postDto);
        }
        return hashtagDto;
    }
}
