package com.example.tiktokproject.services;

import com.example.tiktokproject.controller.SessionManager;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.postDTO.PostEditRequestDTO;
import com.example.tiktokproject.model.dto.postDTO.PostEditResponseDTO;
import com.example.tiktokproject.model.dto.postDTO.PostUploadRequestDTO;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;

    public void uploadPost(PostUploadRequestDTO post) {

    }

    public void deletePost(int postId, HttpSession session){
        checkUserPrivileges(postId, session);
        postRepository.deleteById(postRepository.findById(postId).get().getId());
    }

    public PostEditResponseDTO editPost(PostEditRequestDTO postDto, HttpSession session) {
        checkUserPrivileges(postDto.getId(),session);
        Post post = postRepository.findById(postDto.getId()).get();
        if (postDto.isPublic() != post.isPublic()){
            post.setPublic(postDto.isPublic());
        }
        if (!postDto.getDescription().equals(post.getDescription())){
            post.setDescription(postDto.getDescription());
        }
        postRepository.save(post);
        return modelMapper.map(post, PostEditResponseDTO.class);
    }


    private void checkUserPrivileges(int postId, HttpSession session){
        if (postRepository.findById(postId).isEmpty()){
            throw new NotFoundException("No post found!");
        }
        if (postRepository.findById(postId).get().getOwnerId() != (int) session.getAttribute(SessionManager.USER_ID)){
            throw new UnauthorizedException("You can't delete this post!");
        }
    }


}
