package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.playlistDTO.PlaylistRequestDTO;
import com.example.tiktokproject.model.dto.playlistDTO.PlaylistResponseDTO;
import com.example.tiktokproject.model.dto.playlistDTO.PlaylistWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.userDTO.UserWithoutPostDTO;
import com.example.tiktokproject.model.pojo.Playlist;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.PlaylistRepository;
import com.example.tiktokproject.model.repository.PostRepository;
import com.example.tiktokproject.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    public static final int CREATOR_ROLE_ID = 2;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public PlaylistResponseDTO createPlaylist(User user, PlaylistRequestDTO playlistDto) {
        if (user.getRoleId() != CREATOR_ROLE_ID) {
            throw new UnauthorizedException("Your account have to be creator");
        }
        Playlist playlist = modelMapper.map(playlistDto, Playlist.class);
        playlist.setCreatedTime(LocalDateTime.now());
        playlist.setOwner(user);
        playlistRepository.save(playlist);
        PlaylistResponseDTO response = modelMapper.map(playlist, PlaylistResponseDTO.class);
        response.setUser(modelMapper.map(user, UserWithoutPostDTO.class));
        return response;
    }


    public void deletePlaylist(User user, int playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException("Not found playlist."));
        if (playlist.getOwner().getId() != user.getId()) {
            throw new UnauthorizedException("You can't delete someone else playlist.");
        }
        playlistRepository.delete(playlist);
    }

    public PlaylistResponseDTO editPlaylist(User user, int playlistId, PlaylistRequestDTO playlistDto) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist not found"));
        if (playlist.getOwner().getId() != user.getId()) {
            throw new UnauthorizedException("You can't edit someone else playlist.");
        }
        playlist.setName(playlistDto.getName());
        playlistRepository.save(playlist);
        PlaylistResponseDTO response = modelMapper.map(playlist, PlaylistResponseDTO.class);
        response.setUser(modelMapper.map(user, UserWithoutPostDTO.class));
        return response;
    }

    public PlaylistWithoutOwnerDTO addVideoToPlaylist(User user, int playlistId, int postId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist not found"));
        if (!user.getPlaylists().contains(playlist)) {
            throw new BadRequestException("You can't add posts to another user playlist");
        }
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (playlist.getPosts().contains(post)) {
            throw new BadRequestException("This post is already in this playlist");
        }
        if (post.getOwner().getId() != user.getId()) {
            throw new BadRequestException("This post is not your");
        }
        playlist.addPost(post);
        playlistRepository.save(playlist);
        UserWithoutPostDTO userWithoutPost = modelMapper.map(user, UserWithoutPostDTO.class);
        PlaylistWithoutOwnerDTO dto = modelMapper.map(playlist, PlaylistWithoutOwnerDTO.class);
        dto.setUserWithoutPost(userWithoutPost);
        addPostToPostWithoutOwnerDto(dto, playlist);
        return dto;
    }

    public PlaylistWithoutOwnerDTO removePostFromPlaylist(User user, int postId, int playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist not found"));
        if (!user.getPlaylists().contains(playlist)) {
            throw new BadRequestException("You can't remove posts to another user playlist");
        }
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!playlist.getPosts().contains(post)) {
            throw new BadRequestException("This post is removed already from this playlist");
        }
        playlist.removePost(post);
        playlistRepository.save(playlist);
        UserWithoutPostDTO userWithoutPost = modelMapper.map(user, UserWithoutPostDTO.class);
        PlaylistWithoutOwnerDTO playlistDto = modelMapper.map(playlist, PlaylistWithoutOwnerDTO.class);
        playlistDto.setUserWithoutPost(userWithoutPost);
        addPostToPostWithoutOwnerDto(playlistDto, playlist);
        return playlistDto;
    }


    public List<PlaylistWithoutOwnerDTO> getAllPlaylists(int userId, int pageNumber, int rowsNumber) {
        Pageable page = PageRequest.of(pageNumber, rowsNumber, Sort.by("createdTime").descending());
        List<Playlist> playlists = playlistRepository.findAllByOwnerId(userId, page);
        List<PlaylistWithoutOwnerDTO> playlistsDto = new ArrayList<>();
        User u = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        UserWithoutPostDTO userWithoutPost = modelMapper.map(u, UserWithoutPostDTO.class);
        for (Playlist p : playlists) {
            PlaylistWithoutOwnerDTO playlistWithoutOwnerDTO = modelMapper.map(p, PlaylistWithoutOwnerDTO.class);
            addPostToPostWithoutOwnerDto(playlistWithoutOwnerDTO, p);
            playlistsDto.add(playlistWithoutOwnerDTO);
            playlistWithoutOwnerDTO.setUserWithoutPost(userWithoutPost);
        }
        return playlistsDto;
    }

    public PlaylistWithoutOwnerDTO getPlaylistById(int playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException("Playlist not found"));
        PlaylistWithoutOwnerDTO playlistDto = modelMapper.map(playlist, PlaylistWithoutOwnerDTO.class);
        UserWithoutPostDTO user = modelMapper.map(playlist.getOwner(), UserWithoutPostDTO.class);
        playlistDto.setUserWithoutPost(user);
        addPostToPostWithoutOwnerDto(playlistDto, playlist);
        return playlistDto;
    }

    private void addPostToPostWithoutOwnerDto(PlaylistWithoutOwnerDTO playlistDto, Playlist playlist) {
        List<Post> postsInPlaylist = playlist.getPosts();
        for (Post p : postsInPlaylist) {
            PostWithoutOwnerDTO postDto = modelMapper.map(p, PostWithoutOwnerDTO.class);
            postDto.setPostHaveLikes(p.getPostLikes().size());
            postDto.setPostHaveComments(p.getPostComments().size());
            playlistDto.addPost(postDto);
        }
    }
}
