package com.example.demo.queries;

import org.checkerframework.checker.units.qual.s;
import org.junit.jupiter.api.BeforeEach;
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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class Consulta2_1NavegacaoEFiltrosTests {
    // Consultas Focadas em Relacionamentos (Navegação e Filtro)

    private UsuarioModel usuario;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    Set<MusicaModel> musicasDaPlaylist() {
        ArtistaModel queen = artistaRepository.findByNome("Queen").orElseThrow();
        ArtistaModel ledZeppelin = artistaRepository.findByNome("Led Zeppelin").orElseThrow();

        // Queen
        MusicaModel theShowMustGoOn = new MusicaModel();
        theShowMustGoOn.setTitulo("The Show Must Go On");
        theShowMustGoOn.setDuracaoSegundos(270);
        theShowMustGoOn.setArtista(queen);
        theShowMustGoOn = musicaRepository.save(theShowMustGoOn);

        MusicaModel bohemianRhapsody = musicaRepository.findByTitulo("Bohemian Rhapsody").orElseThrow();

        // Led Zeppelin
        MusicaModel wholeLottaLove = new MusicaModel();
        wholeLottaLove.setTitulo("Whole Lotta Love");
        wholeLottaLove.setDuracaoSegundos(333);
        wholeLottaLove.setArtista(ledZeppelin);
        wholeLottaLove = musicaRepository.save(wholeLottaLove);

        MusicaModel stairwayToHeaven = musicaRepository.findByTitulo("Stairway to Heaven").orElseThrow();

        return Set.of(bohemianRhapsody, stairwayToHeaven, theShowMustGoOn, wholeLottaLove);
    }

    void setUp() {
        usuario = usuarioRepository.findByUsername("Alexandre").orElseThrow();

        PlaylistModel playlist = new PlaylistModel();
        playlist.setNome("As Brabas do Alexandre");
        playlist.setDataCriacao(null); // default current timestamp
        playlist.setUsuario(usuario);

        Set<MusicaModel> musicas = musicasDaPlaylist();
        playlist.addMusicas(musicas);

        playlistRepository.save(playlist);
    }

    UsuarioModel getUsuarioOrCreate(String username) {
        return usuarioRepository.findByUsername(username).orElseGet(() -> {
            UsuarioModel novoUsuario = new UsuarioModel();
            novoUsuario.setUsername(username);
            novoUsuario.setEmail(username.toLowerCase() + "@example.com");
            return usuarioRepository.save(novoUsuario);
        });
    }

    /*
     * Playlists de um Usuário Específico: Implemente uma função para listar todas
     * as Playlists de um USUARIO específico, usando o username como filtro (ex:
     * 'Pablo'). O retorno deve incluir o nome da Playlist e a data de criação.
     */
    @Test
    void testGetPlaylistFromUser() {
        UsuarioModel foundUser = getUsuarioOrCreate("Alexandre");

        Set<PlaylistModel> playlists = playlistRepository.findByUsuarioUsername(foundUser.getUsername());

        for (PlaylistModel playlist : playlists) {
            System.out.println(playlist);
        }
    }

    /*
     * Músicas em Playlists de um Artista: Encontre todas as Músicas que pertencem a
     * qualquer Playlist criada por um USUARIO específico (ex: 'Josue'), e cujo
     * ARTISTA seja 'Queen'. Esta consulta requer atravessar múltiplos
     * relacionamentos e aplicar filtros em diferentes entidades.
     */
    @Test
    void testPlaylistUserRelation() {
        Set<MusicaModel> musicasDoQueenEmPlaylistDoAlexandre = musicaRepository
                .musicasDeUmArtistaEmPlaylistDeUsuario("Queen", "Alexandre");

        for (MusicaModel musica : musicasDoQueenEmPlaylistDoAlexandre) {
            System.out.println(musica);

            assertThat(musica.getArtista().getNome()).isEqualTo("Queen");
        }
    }

    /*
     * Contagem de Músicas por Playlist: Liste o nome de todas as Playlists e o
     * número total de Músicas que cada uma contém. A listagem deve ser ordenada da
     * Playlist mais longa para a mais curta. (Foco em agregação e manipulação da
     * chave composta da Playlist).
     */
    @Test
    void testPlaylistSizes() {
        List<Pair<String, Long>> playlistNamesAndSizes = playlistRepository.getPlaylistsNamesAndSizes();

        for (Pair<String, Long> par : playlistNamesAndSizes) {
            System.out.println(par);
        }
    }

    /*
     * Artistas Sem Músicas em Playlists: Identifique e liste todos os Artistas que
     * não possuem nenhuma de suas Músicas adicionadas a nenhuma Playlist no
     * sistema. (Foco em operadores NOT IN, LEFT JOIN ou EXCEPT).
     */
    @Test
    void artistasWithNoMusicsOnPlaylists() {
        Set<ArtistaModel> artistas = artistaRepository.artistasSemMusicasEmQualquerPlaylist();
        assertThat(artistas).hasSizeGreaterThanOrEqualTo(1); // artista que foi adicionado

        for (ArtistaModel artista : artistas) {
            System.out.println(artista);

            Set<MusicaModel> musicasSemPlaylist = artista.getMusicas();

            for (MusicaModel musica : musicasSemPlaylist) {
                System.out.println("  Música sem playlist: " + musica);
            }

            assertThat(musicasSemPlaylist).extracting(MusicaModel::getPlaylists)
                    .allMatch(playlists -> playlists.isEmpty());
        }
    }

}
