package com.example.demo.models.Ids;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;

@Embeddable
public class PlaylistID implements Serializable {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playlist_playlist_id_seq")
    @SequenceGenerator(name = "playlist_playlist_id_seq",  sequenceName = "playlist_playlist_id_seq", allocationSize = 1)
    @Column(name="playlist_id")
    private Long playlistId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    // Getters and Setters
    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistID that = (PlaylistID) o;

        if (!playlistId.equals(that.playlistId)) return false;
        return usuarioId.equals(that.usuarioId);
    }

    @Override
    public int hashCode() {
        int result = playlistId.hashCode();
        result = 31 * result + usuarioId.hashCode();
        return result;
    }

}
