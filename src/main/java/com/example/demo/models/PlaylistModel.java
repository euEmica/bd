package com.example.demo.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.models.Ids.PlaylistID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PLAYLIST")
public class PlaylistModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public PlaylistModel() {
        this.id = new PlaylistID();
        this.musicas = new ArrayList<>();
    }

    @EmbeddedId
    private PlaylistID id;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioModel usuario;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MusicaPlaylistModel> musicas;

    public void setId(PlaylistID id) {
        this.id = id;
    }

    public PlaylistID getId() {
        return id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public Set<MusicaPlaylistModel> getMusicas() {
        return Set.copyOf(musicas);
    }

    public void setMusicas(Set<MusicaPlaylistModel> musicas) {
        this.musicas = new ArrayList<>(musicas);
    }

    public void addMusicas(Set<MusicaModel> musicas) {
        for (MusicaModel musica : musicas) {
            addMusica(musica);
        }
    }

    public void addMusica(MusicaModel musicaPlaylist) {
        MusicaPlaylistModel musicaPlaylistModel = new MusicaPlaylistModel();
        musicaPlaylistModel.setMusica(musicaPlaylist);
        musicaPlaylistModel.setPlaylist(this);

        Integer maxOrdem = this.musicas.stream()
                .map(MusicaPlaylistModel::getOrdem)
                .max(Integer::compareTo)
                .orElse(0);
        musicaPlaylistModel.setOrdem(maxOrdem + 1);
        // adjustMusicasOrder();

        this.musicas.add(musicaPlaylistModel);
    }

    public void removeMusica(MusicaModel musicaPlaylist) {
        this.musicas.removeIf(mp -> mp.getMusica().equals(musicaPlaylist) && mp.getPlaylist().equals(this));
        // adjustMusicasOrder();
    }

    public void adjustMusicasOrder() {
        List<MusicaPlaylistModel> musicas = this.musicas;
        musicas.sort((mp1, mp2) -> mp1.getOrdem().compareTo(mp2.getOrdem()));

        Integer ordem = 1;
        for (MusicaPlaylistModel mp : musicas) {
            mp.setOrdem(ordem);
            ordem++;
        }
    }

    @Override
    public String toString() {
        return "PlaylistModel{" +
                "id=" + id +
                ", dataCriacao=" + dataCriacao +
                ", nome='" + nome + '\'' +
                ", usuario=" + usuario.getUsername() +
                ", musicas=" + musicas.size() +
                '}';
    }

}
