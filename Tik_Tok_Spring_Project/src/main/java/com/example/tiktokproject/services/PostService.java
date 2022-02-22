package com.example.tiktokproject.services;

import com.example.tiktokproject.controller.SessionManager;
import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.postDTO.*;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.example.tiktokproject.model.pojo.Hashtag;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.HashtagRepository;
import com.example.tiktokproject.model.repository.PostRepository;
import com.example.tiktokproject.model.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final long MAX_UPLOAD_SIZE = 250 * 1024 * 1024;
    private static final String UPLOAD_FOLDER = "uploads";
    public static final String HASHTAG_REGEX = "#[A-Za-z0-9_]+";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private UserRepository userRepository;

    public PostUploadResponseDTO uploadPostVideo(int postId, MultipartFile file) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = System.nanoTime() + postId + "." + extension;
        if (file.getSize() > MAX_UPLOAD_SIZE) {
            throw new BadRequestException("Too big video size. The maximum video size is 250MB.");
        }
        if (!("mp4".equals(extension))) {
            throw new BadRequestException("Wrong video format.You can upload only .mp4.");
        }
        try {
            Files.copy(file.getInputStream(), new File(UPLOAD_FOLDER + File.separator + fileName).toPath());
        } catch (IOException e) {
            throw new com.example.tiktokproject.exceptions.IOException("Server file system error.");
        }
        p.setVideoUrl(fileName);
        postRepository.save(p);
        return modelMapper.map(p, PostUploadResponseDTO.class);
    }

    public PostUploadResponseDTO makePost(PostUploadRequestDTO post) {
        if (post.getDescription() != null) {
            if (post.getDescription().isBlank()) {
                throw new BadRequestException("Only spaces in description is not allowed");
            }
        }
        Post p = modelMapper.map(post, Post.class);
        p.setUploadDate(LocalDateTime.now());

        String[] spaces = post.getDescription().split(" ");
        ArrayList<String> hashtags = new ArrayList<>();
        for (String s : spaces) {
            if (s.matches(HASHTAG_REGEX)) {
                hashtags.add(s);
            }
        }
        if (hashtags.size() > 0) {
            for (String hashtag : hashtags) {
                Hashtag hash = new Hashtag();
                hash.setTitle(hashtag);
//                hash.addPost(p);
                hashtagRepository.save(hash);
            }
        }

        postRepository.save(p);
        return modelMapper.map(p, PostUploadResponseDTO.class);
    }

    public void deletePost(int postId, HttpSession session) {
        checkUserPrivileges(postId, session);
        postRepository.deleteById(postRepository.findById(postId).get().getId());
    }

    public PostEditResponseDTO editPost(PostEditRequestDTO postDto, HttpSession session) {
        checkUserPrivileges(postDto.getId(), session);
        Post post = postRepository.findById(postDto.getId()).get();
        if (postDto.isPublic() != post.isPublic()) {
            post.setPublic(postDto.isPublic());
        }
        if (!postDto.getDescription().equals(post.getDescription())) {
            post.setDescription(postDto.getDescription());
        }
        postRepository.save(post);
        return modelMapper.map(post, PostEditResponseDTO.class);
    }


    public PostWithOwnerDTO getPost(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!post.isPublic()) {
            throw new UnauthorizedException("This video is private!");
        }
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
        UserWithoutPostDTO userDto = modelMapper.map(post.getOwner(), UserWithoutPostDTO.class);
        PostWithOwnerDTO postDto = modelMapper.map(post, PostWithOwnerDTO.class);
        postDto.setOwner(userDto);
        postDto.setLikes(post.getPostLikes().size());
        postDto.setComments(post.getComments().size());
        return postDto;
    }

    public List<PostWithOwnerDTO> getAllPostsSortByUploadDate(int id) {
        List<Post> posts = postRepository.findPostsByUploadDate(id);
        List<PostWithOwnerDTO> postsWithOwner = new ArrayList<>();
        for (Post p : posts) {
            PostWithOwnerDTO post = modelMapper.map(p, PostWithOwnerDTO.class);
            post.setOwner(modelMapper.map(p.getOwner(), UserWithoutPostDTO.class));
            postsWithOwner.add(post);
        }
        return postsWithOwner;
    }

    public void likePost(int postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not found post"));
        if (post.getPostLikes().contains(user)) {
            throw new BadRequestException("You already liked this post.");
        }
        if (user.getUserLikedPosts().contains(post)) {
            throw new BadRequestException("You already liked this post");
        }
        post.addLike(user);
        user.addLikedPost(post);
        userRepository.save(user);
        postRepository.save(post);
    }

    public void unlikePost(int postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not found post"));
        if (!post.getPostLikes().contains(user)) {
            throw new BadRequestException("You already unlike this post.");
        }
        if (!user.getUserLikedPosts().contains(post)) {
            throw new BadRequestException("You already unliked this post");
        }
        post.removeLike(user);
        user.removeLikedPost(post);
        userRepository.save(user);
        postRepository.save(post);
    }

    private void checkUserPrivileges(int postId, HttpSession session) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new NotFoundException("No post found!");
        }
        if (postRepository.findById(postId).get().getOwner().getId() != (int) session.getAttribute(SessionManager.USER_ID)) {
            throw new UnauthorizedException("You can't delete this post!");
        }
    }
}
