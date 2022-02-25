package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.model.dto.hashtagDTO.HashtagResponseDTO;
import com.example.tiktokproject.model.dto.postDTO.PostWithOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.example.tiktokproject.model.pojo.Hashtag;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.repository.HashtagRepository;
import com.example.tiktokproject.model.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagService {

    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;

    public HashtagResponseDTO getAllPostsByHashtag(String title, int rowsNumber, int pageNumber) {
        Hashtag hashtag = hashtagRepository.findHashtagByTitle(title).orElseThrow(() -> new NotFoundException("Hashtag not found"));
        Pageable page = PageRequest.of(rowsNumber, pageNumber, Sort.by("title").descending());
        HashtagResponseDTO hashtagDto = modelMapper.map(hashtag, HashtagResponseDTO.class);
        List<Post> hashtagPosts = postRepository.findAllBy(hashtag, page);
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
