package com.example.demo.repositories;

import org.springframework.stereotype.Repository;

import com.example.demo.models.MusicaPlaylistModel;
import com.example.demo.models.Ids.MusicaPlaylistID;

@Repository
public interface MusicaPlaylistRepository extends RepositoryInterface<MusicaPlaylistModel, MusicaPlaylistID> {
}
