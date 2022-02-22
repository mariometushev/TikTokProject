package com.example.tiktokproject.model.repository;

import com.example.tiktokproject.model.pojo.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    @Query(value = "SELECT * FROM playlists AS p WHERE p.owner_id = :id", nativeQuery = true)
    List<Playlist> findAllByOwnerId(@Param("id") Integer id);

}
