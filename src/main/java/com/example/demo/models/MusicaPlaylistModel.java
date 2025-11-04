package com.example.demo.models;

import java.io.Serializable;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "MUSICA_PLAYLIST")
@AssociationOverrides({
		@AssociationOverride(name = "pk.musica",
			joinColumns = @JoinColumn(name = "musica_id")),
		@AssociationOverride(name = "pk.artista",
			joinColumns = @JoinColumn(name = "artista_id")),
        @AssociationOverride(name = "pk.usuario",
            joinColumns = @JoinColumn(name = "usuario_id")) })
public class MusicaPlaylistModel implements Serializable {
    private static final long serialVersionUID = 1L;


    // Attributes

    @Id
    @Column(name = "musica_id", nullable = false)
    private Long musicaId;

    @Id
    @Column(name = "artista_id", nullable = false)
    private Long artistaId;

    @Id
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "ordem_na_playlist", nullable = false)
    private Integer ordem;


    // Getters and Setters

    public Long getMusicaId() {
        return musicaId;
    }

    public void setMusicaId(Long musicaId) {
        this.musicaId = musicaId;
    }

    public Long getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(Long artistaId) {
        this.artistaId = artistaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }


    // Override hashCode and equals for composite key

    @Override
    public int hashCode() {
        return Integer.parseInt(String.valueOf(Long.hashCode(musicaId)) + String.valueOf(Long.hashCode(artistaId))
                + String.valueOf(Long.hashCode(usuarioId)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        MusicaPlaylistModel other = (MusicaPlaylistModel) obj;
        return this.musicaId.equals(other.getMusicaId()) && this.artistaId.equals(other.getArtistaId())
                && this.usuarioId.equals(other.getUsuarioId());
    }

}
