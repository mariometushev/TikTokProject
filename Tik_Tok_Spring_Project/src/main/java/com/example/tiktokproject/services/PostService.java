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
import lombok.Synchronized;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final long MAX_UPLOAD_SIZE = 50 * 1024 * 1024;
    private static final String UPLOAD_FOLDER = "uploads";
    public static final String HASHTAG_REGEX = "#[A-Za-z0-9_]+";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HashtagRepository hashtagRepository;

    public PostUploadResponseDTO uploadPostVideo(int postId, MultipartFile file) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (p.getVideoUrl() != null) {
            throw new BadRequestException("You can't upload more than one video files in one post");
        }
        Tika tika = new Tika();
        String realFileExtension = null;
        try {
            realFileExtension = tika.detect(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + postId + "." + extension;
        if (file.getSize() > MAX_UPLOAD_SIZE) {
            throw new BadRequestException("Too big video size. The maximum video size is 50MB.");
        }
        if (!("video/mp4".equals(realFileExtension))) {
            throw new BadRequestException("Wrong video format.You can upload only .mp4.");
        }
        try {
            Files.copy(file.getInputStream(), new File(UPLOAD_FOLDER + File.separator + fileName).toPath());
        } catch (IOException e) {
            throw new com.example.tiktokproject.exceptions.IOException("Server file system error.");
        }
        p.setVideoUrl(fileName);
        postRepository.save(p);
        PostUploadResponseDTO response = modelMapper.map(p, PostUploadResponseDTO.class);
        response.setOwnerWithoutPost(modelMapper.map(p.getOwner(), UserWithoutPostDTO.class));
        return response;
    }

    public PostUploadResponseDTO makePost(PostUploadRequestDTO post, User u) {
        checkForNullButNotBlankInput(post.getDescription());
        Post p = modelMapper.map(post, Post.class);
        p.setUploadDate(LocalDateTime.now());
        p.setOwner(u);
        if (p.getDescription() != null && !p.getDescription().isBlank()) {
            checkForHashtags(p, true);
        }
        postRepository.save(p);
        PostUploadResponseDTO postUpload = modelMapper.map(p, PostUploadResponseDTO.class);
        UserWithoutPostDTO userWithoutPost = modelMapper.map(u, UserWithoutPostDTO.class);
        postUpload.setOwnerWithoutPost(userWithoutPost);
        return postUpload;
    }

    public void deletePost(int postId, HttpSession session) {
        checkUserPrivileges(postId, session);
        postRepository.delete(postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found")));
    }

    public PostEditResponseDTO editPost(int postId, PostEditRequestDTO postDto, HttpSession session) {
        checkUserPrivileges(postId, session);
        checkForNullButNotBlankInput(postDto.getDescription());
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (postDto.isPrivacy() != post.isPrivacy()) {
            post.setPrivacy(postDto.isPrivacy());
        }
        if (postDto.getDescription() != null && !postDto.getDescription().isBlank()) {
            checkForHashtags(post, false);
            post.setDescription(postDto.getDescription());
            checkForHashtags(post, true);
        }
        postRepository.save(post);
        PostEditResponseDTO response = modelMapper.map(post, PostEditResponseDTO.class);
        response.setUserWithoutPost(modelMapper.map(post.getOwner(), UserWithoutPostDTO.class));
        return response;
    }


    public PostWithOwnerDTO getPost(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!post.isPrivacy()) {
            throw new UnauthorizedException("This video is private!");
        }
        post.setViews(post.getViews() + 1);//TODO synchronized???
        postRepository.save(post);
        UserWithoutPostDTO userDto = modelMapper.map(post.getOwner(), UserWithoutPostDTO.class);
        PostWithOwnerDTO postDto = modelMapper.map(post, PostWithOwnerDTO.class);
        postDto.setUserWithoutPost(userDto);
        postDto.setPostHaveLikes(post.getPostLikes().size());
        postDto.setPostHaveComments(post.getPostComments().size());
        return postDto;
    }

    public List<PostWithOwnerDTO> getAllPostsByUserIdSortByUploadDate(int id, int rowsNumber, int pageNumber) {
        Pageable page = PageRequest.of(rowsNumber, pageNumber);
        List<Post> posts = postRepository.findPostsByUploadDate(id, page);
        return mappingPostToPostWithOwnerDTO(posts);
    }

    public List<PostWithOwnerDTO> getAllPostsSortByUploadDate(int pageNumber, int rowsNumber) {
        Pageable page = PageRequest.of(pageNumber, rowsNumber, Sort.by("upload_date"));
        List<Post> posts = postRepository.findAllPosts(page);
        return mappingPostToPostWithOwnerDTO(posts);
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
        postRepository.save(post);
    }

    private List<PostWithOwnerDTO> mappingPostToPostWithOwnerDTO(List<Post> posts){
        List<PostWithOwnerDTO> postsWithOwner = new ArrayList<>();
        for (Post p : posts) {
            PostWithOwnerDTO post = modelMapper.map(p, PostWithOwnerDTO.class);
            post.setUserWithoutPost(modelMapper.map(p.getOwner(), UserWithoutPostDTO.class));
            post.setPostHaveComments(p.getPostComments().size());
            post.setPostHaveLikes(p.getPostLikes().size());
            postsWithOwner.add(post);
        }
        return postsWithOwner;
    }

    private void checkForHashtags(Post p, boolean addHashtag) {
        String[] spaces = p.getDescription().split(" ");
        ArrayList<String> hashtags = new ArrayList<>();
        for (String s : spaces) {
            if (s.matches(HASHTAG_REGEX)) {
                hashtags.add(s);
            }
        }
        if (hashtags.size() > 0) {
            for (String hashtag : hashtags) {
                Hashtag hash;
                if (hashtagRepository.findHashtagByTitle(hashtag).isPresent()) {
                    hash = hashtagRepository.findHashtagByTitle(hashtag).get();
                } else {
                    hash = new Hashtag();
                    hash.setTitle(hashtag);
                    hashtagRepository.save(hash);
                }
                if (addHashtag) {
                    p.addHashtag(hash);
                } else {
                    p.removeHashtag(hash);
                }
            }
        }
    }

    private void checkUserPrivileges(int postId, HttpSession session) {
        if (postRepository.findById(postId).isEmpty()) {
            throw new NotFoundException("Post not found!");
        }
        if (postRepository.findById(postId).get().getOwner().getId() != (int) session.getAttribute(SessionManager.USER_ID)) {
            throw new UnauthorizedException("You can't delete another user post!");
        }
    }

    private void checkForNullButNotBlankInput(String text) {
        if (text.trim().isEmpty()) {
            throw new BadRequestException("This field can't be only white spaces");
        }
    }
}
