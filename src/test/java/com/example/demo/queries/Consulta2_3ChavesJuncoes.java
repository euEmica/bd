package com.example.demo.queries;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.util.Pair;
import org.springframework.test.annotation.Rollback;

import com.example.demo.models.*;
import com.example.demo.repositories.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class Consulta2_3ChavesJuncoes {

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /*
     * Busca em Tabela de Junção com Atributos Extras: Liste o título de todas as
     * Músicas na playlist 'Rock do Pablo', incluindo a ordem_na_playlist de cada
     * música.
     */
    @Test
    void listMusicsAndOrder() {
        PlaylistModel playlist = playlistRepository.findByNome("Rock do Pablo").orElseThrow();
        List<Pair<String, Long>> musicas = playlistRepository.getPlaylistsMusicasAndOrder(playlist.getNome());

        assertThat(musicas).isNotEmpty();
        List<Long> ordens = musicas.stream()
                .map(Pair::getSecond)
                .collect(Collectors.toList());
        assertThat(ordens).isSorted();
    }

    /*
     * Busca por Chave Composta Invertida: Encontre o username do Usuário que é o
     * dono da Playlist que contém a MUSICA 'Bohemian Rhapsody'. O filtro deve
     * começar pela MUSICA e navegar de volta para o USUARIO.
     */
    @Test
    void getUsernameOfPlaylistsWithMusic() {
        MusicaModel musica = musicaRepository.findByTitulo("Bohemian Rhapsody").orElseThrow();
        Set<String> usernames = usuarioRepository.findUsernameWithMusicInPlaylists(musica.getTitulo());

        assertThat(usernames).isNotEmpty();

        assertThat(usernames).containsAll(musica.getPlaylists().stream()
                .map(MusicaPlaylistModel::getPlaylist)
                .map(PlaylistModel::getUsuario)
                .map(UsuarioModel::getUsername)
                .collect(Collectors.toSet()));
    }
}
