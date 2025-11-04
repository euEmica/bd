package com.example.demo.repositories;

import org.springframework.stereotype.Repository;

import com.example.demo.models.PlaylistModel;
import com.example.demo.models.Ids.PlaylistID;

@Repository
public interface PlaylistRepository extends RepositoryInterface<PlaylistModel, PlaylistID> {}
