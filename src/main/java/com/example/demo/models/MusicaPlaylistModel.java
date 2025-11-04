package com.example.demo.models;

import java.io.Serializable;

import com.example.demo.models.Ids.MusicaPlaylistID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "MUSICA_PLAYLIST")
public class MusicaPlaylistModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public MusicaPlaylistModel() {
        this.id = new MusicaPlaylistID();
    }

    @EmbeddedId
    private MusicaPlaylistID id;

    // Attributes

    @Column(name = "ordem_na_playlist", nullable = false)
    private Integer ordem;

    @MapsId("musicaId")
    @ManyToOne
    private MusicaModel musica;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumns({
        @JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id", nullable = false),
        @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", nullable = false)
    })
    private PlaylistModel playlist;

    // Getters and Setters
    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public MusicaPlaylistID getId() {
        return id;
    }

    public void setId(MusicaPlaylistID id) {
        this.id = id;
    }

    public MusicaModel getMusica() {
        return musica;
    }

    public void setMusica(MusicaModel musica) {
        this.musica = musica;

        this.id.setMusicaId(musica.getId());
    }

    public PlaylistModel getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistModel playlist) {
        this.playlist = playlist;

        this.id.setPlaylistId(playlist.getId());
    }
}
