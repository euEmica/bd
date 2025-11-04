package com.example.demo.models.Ids;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;

@Embeddable
public class MusicaPlaylistID implements Serializable {

    @Column(name = "musica_id", nullable = false)
    private Long musicaId;

    @Embedded
    private PlaylistID playlistId;

    // Getters and Setters
    public Long getMusicaId() {
        return musicaId;
    }

    public void setMusicaId(Long musicaId) {
        this.musicaId = musicaId;
    }

    public PlaylistID getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(PlaylistID playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicaPlaylistID that = (MusicaPlaylistID) o;

        if (!musicaId.equals(that.musicaId)) return false;
        if (!playlistId.equals(that.playlistId)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(musicaId, playlistId);
    }
}
