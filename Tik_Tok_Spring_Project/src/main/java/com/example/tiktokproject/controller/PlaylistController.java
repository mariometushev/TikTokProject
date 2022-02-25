package com.example.tiktokproject.controller;

import com.example.tiktokproject.model.dto.playlistDTO.PlaylistRequestDTO;
import com.example.tiktokproject.model.dto.playlistDTO.PlaylistResponseDTO;
import com.example.tiktokproject.model.dto.playlistDTO.PlaylistWithoutOwnerDTO;
import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/users/{id}/createPlaylist")
    public ResponseEntity<PlaylistResponseDTO> createPlaylist(@PathVariable(name = "id") int userId,
                                                              @Valid @RequestBody PlaylistRequestDTO playlistDto,
                                                              HttpServletRequest request) {
        sessionManager.validateLogin(request);
        sessionManager.validateUserId(request.getSession(), userId);
        User u = sessionManager.getSessionUser(request.getSession());
        return new ResponseEntity<>(playlistService.createPlaylist(u, playlistDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/deletePlaylist/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable(name = "id") int playlistId, HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User user = sessionManager.getSessionUser(request.getSession());
        playlistService.deletePlaylist(user, playlistId);
        return new ResponseEntity<>("Delete playlist request was successful", HttpStatus.ACCEPTED);
    }

    @PutMapping("/playlists/{id}/edit")
    public ResponseEntity<PlaylistResponseDTO> editPlaylist(@PathVariable(name = "id") int playlistId,
                                                            @Valid @RequestBody PlaylistRequestDTO playlistDto,
                                                            HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User user = sessionManager.getSessionUser(request.getSession());
        return new ResponseEntity<>(playlistService.editPlaylist(user, playlistId, playlistDto), HttpStatus.ACCEPTED);
    }

    @PostMapping("/posts/{pId}/addToPlaylist/{plId}")
    public ResponseEntity<PlaylistWithoutOwnerDTO> addVideoToPlaylist(@PathVariable(name = "pId") int postId,
                                                                      @PathVariable(name = "plId") int playlistId,
                                                                      HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User user = sessionManager.getSessionUser(request.getSession());
        return new ResponseEntity<>(playlistService.addVideoToPlaylist(user, playlistId, postId), HttpStatus.ACCEPTED);
    }

    @PostMapping("/posts/{pId}/removeFromPlaylist/{plId}")
    public ResponseEntity<PlaylistWithoutOwnerDTO> removePostFromPlaylist(@PathVariable(name = "pId") int postId,
                                                                          @PathVariable(name = "plId") int playlistId,
                                                                          HttpServletRequest request) {
        sessionManager.validateLogin(request);
        User user = sessionManager.getSessionUser(request.getSession());
        return new ResponseEntity<>(playlistService.removePostFromPlaylist(user, postId, playlistId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/users/{id}/playlists")
    public ResponseEntity<List<PlaylistWithoutOwnerDTO>> getAllPlaylists(@PathVariable(name = "id") int userId,
                                                                         @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                                         @RequestParam(name = "rowsNumber", defaultValue = "10") int rowsNumber) {
        return new ResponseEntity<>(playlistService.getAllPlaylists(userId,pageNumber,rowsNumber), HttpStatus.OK);
    }

    @GetMapping("playlists/{id}")
    public ResponseEntity<PlaylistWithoutOwnerDTO> getPlaylistById(@PathVariable(name = "id") int playlistId) {
        return new ResponseEntity<>(playlistService.getPlaylistById(playlistId), HttpStatus.OK);
    }

}
