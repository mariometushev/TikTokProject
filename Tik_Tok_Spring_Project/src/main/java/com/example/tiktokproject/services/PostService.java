package com.example.tiktokproject.services;

import com.example.tiktokproject.controller.SessionManager;
import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.postDTO.PostEditRequestDTO;
import com.example.tiktokproject.model.dto.postDTO.PostEditResponseDTO;
import com.example.tiktokproject.model.dto.postDTO.PostUploadRequestDTO;
import com.example.tiktokproject.model.dto.postDTO.PostUploadResponseDTO;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.repository.PostRepository;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class PostService {

    private static final long MAX_UPLOAD_SIZE = 250 * 1024 * 1024;
    private static final String UPLOAD_FOLDER = "uploads";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;

    public PostUploadResponseDTO uploadPost(PostUploadRequestDTO postDto, MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = (System.nanoTime()+postDto.getOwnerId()) + "." + extension;
        if (file.getSize() > MAX_UPLOAD_SIZE){
            throw new BadRequestException("Too big video size. The maximum video size is 250MB.");
        }
        if (!("mp4".equals(extension))){
            throw new BadRequestException("Wrong video format.You can upload only .mp4.");
        }
        try {
            Files.copy(file.getInputStream(), new File(UPLOAD_FOLDER + File.separator + fileName).toPath());
        } catch (IOException e) {
            throw new com.example.tiktokproject.exceptions.IOException("Server file system error.");
        }
        Post post = modelMapper.map(postDto, Post.class);
        post.setUploadDate(LocalDateTime.now());
        post.setVideoUrl(fileName);
        postRepository.save(post);
        return modelMapper.map(post, PostUploadResponseDTO.class);
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
