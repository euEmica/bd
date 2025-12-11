package com.example.demo.dtos.playlists;

public class PlaylistDurationDTO {
    private final String playlistNome;
    private final String usuarioNome;
    private final Long duracaoTotal;

    public PlaylistDurationDTO(String playlistNome, String usuarioNome, Long duracaoTotal) {
        this.playlistNome = playlistNome;
        this.usuarioNome = usuarioNome;
        this.duracaoTotal = duracaoTotal;
    }

    public String getPlaylistNome() {
        return playlistNome;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public Long getDuracaoTotal() {
        return duracaoTotal;
    }

    @Override
    public String toString() {
        return "PlaylistDurationDTO{" +
                "playlistNome='" + playlistNome + '\'' +
                ", usuarioNome='" + usuarioNome + '\'' +
                ", duracaoTotal=" + duracaoTotal +
                '}';
    }
}
