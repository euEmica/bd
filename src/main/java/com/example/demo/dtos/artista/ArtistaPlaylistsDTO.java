package com.example.demo.dtos.artista;

import com.example.demo.models.ArtistaModel;

public class ArtistaPlaylistsDTO {
    private final ArtistaModel artista;
    private final Long totalPlaylists;

    public ArtistaPlaylistsDTO(ArtistaModel artista, Long totalPlaylists) {
        this.artista = artista;
        this.totalPlaylists = totalPlaylists;
    }

    public ArtistaModel getArtista() {
        return artista;
    }

    public Long getTotalPlaylists() {
        return totalPlaylists;
    }

    @Override
    public String toString() {
        return "ArtistaPlaylistsDTO{" +
                "artista=" + artista +
                ", totalPlaylists=" + totalPlaylists +
                '}';
    }
}
