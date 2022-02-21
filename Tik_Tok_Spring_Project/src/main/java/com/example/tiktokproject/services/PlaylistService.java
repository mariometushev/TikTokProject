package com.example.tiktokproject.services;

import com.example.tiktokproject.exceptions.BadRequestException;
import com.example.tiktokproject.exceptions.NotFoundException;
import com.example.tiktokproject.exceptions.UnauthorizedException;
import com.example.tiktokproject.model.dto.playlistDTO.PlaylistRequestDTO;
import com.example.tiktokproject.model.dto.playlistDTO.PlaylistResponseDTO;
import com.example.tiktokproject.model.dto.playlistDTO.PlaylistWithoutOwnerDTO;
import com.example.tiktokproject.model.dto.postDTO.PostWithoutOwnerDTO;
import com.example.tiktokproject.model.pojo.Playlist;
import com.example.tiktokproject.model.pojo.Post;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.PlaylistRepository;
import com.example.tiktokproject.model.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlaylistService {

    public static final int CREATOR_ROLE_ID = 2;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private PostRepository postRepository;

    public PlaylistResponseDTO createPlaylist(User user, PlaylistRequestDTO playlistDto) {
        if (user.getRoleId() != CREATOR_ROLE_ID) {
            throw new UnauthorizedException("Your account have to be creator");
        }
        Playlist playlist = modelMapper.map(playlistDto, Playlist.class);
        playlist.setCreatedTime(LocalDateTime.now());
        playlist.setOwner(user);
        playlistRepository.save(playlist);
        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }


    public void deletePlaylist(User user, int playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException("Not found playlist."));
        if (playlist.getOwner().getId() != user.getId()) {
            throw new UnauthorizedException("You can't delete someone else playlist.");
        }
        playlistRepository.delete(playlist);
    }

    public PlaylistResponseDTO editPlaylist(User user, int playlistId, PlaylistRequestDTO playlistDto) {
        Playlist playlist = playlistRepository.getById(playlistId);
        if (playlist.getOwner().getId() != user.getId()) {
            throw new UnauthorizedException("You can't edit someone else playlist.");
        }
        playlist.setName(playlistDto.getName());
        playlistRepository.save(playlist);
        return modelMapper.map(playlist, PlaylistResponseDTO.class);
    }

    public PlaylistWithoutOwnerDTO addVideoToPlaylist(User user, int playlistId, int postId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new NotFoundException("playlist not found"));
        if (!user.getPlaylists().contains(playlist)) {
            throw new BadRequestException("You can't add posts to another user playlist");
        }
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("post not found"));
        if (playlist.getPosts().contains(post)) {
            throw new BadRequestException("This post is already in this playlist");
        }
        playlist.addPost(post);
        playlistRepository.save(playlist);
        PlaylistWithoutOwnerDTO dto = modelMapper.map(playlist, PlaylistWithoutOwnerDTO.class);
        for (Post p : playlist.getPosts()) {
            PostWithoutOwnerDTO postDTO = modelMapper.map(p, PostWithoutOwnerDTO.class);
            postDTO.setPostLikes(p.getPostLikes().size());
            postDTO.setComments(p.getComments().size());
            dto.addPost(postDTO);
        }
        return dto;
    }
}
