package com.example.demo.queries;

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

    @BeforeEach
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

    void createNewArtista() {
        ArtistaModel artista = new ArtistaModel();
        artista.setNacionalidade("Brasileira");
        artista.setNome("Sem Musicas Em Playlists");
        artista = artistaRepository.save(artista);

        MusicaModel lancamento = new MusicaModel();
        lancamento.setArtista(artista);
        lancamento.setDuracaoSegundos(255);
        lancamento.setTitulo("Novo hit");

        musicaRepository.save(lancamento);
    }

    /*
     * Playlists de um Usuário Específico: Implemente uma função para listar todas
     * as Playlists de um USUARIO específico, usando o username como filtro (ex:
     * 'Pablo'). O retorno deve incluir o nome da Playlist e a data de criação.
     */
    @Test
    void testGetPlaylistFromUser() {
        UsuarioModel foundUser = usuarioRepository.findByUsername(usuario.getUsername()).orElseThrow();

        Set<PlaylistModel> playlists = playlistRepository.findByUsuarioUsername(foundUser.getUsername());
        assertThat(playlists).isNotEmpty();

        PlaylistModel foundPlaylist = playlists.iterator().next();
        assertThat(foundPlaylist.getNome()).isEqualTo("Clássicos do Alexandre");
        assertThat(foundPlaylist.getDataCriacao()).isNotNull();
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
        assertThat(musicasDoQueenEmPlaylistDoAlexandre).isNotEmpty();
        assertThat(musicasDoQueenEmPlaylistDoAlexandre).hasSize(2);
        assertThat(musicasDoQueenEmPlaylistDoAlexandre)
                .extracting(MusicaModel::getTitulo)
                .containsExactlyInAnyOrder("Bohemian Rhapsody", "The Show Must Go On");
        assertThat(musicasDoQueenEmPlaylistDoAlexandre).extracting(MusicaModel::getArtista)
                .extracting(ArtistaModel::getNome).containsOnly("Queen");
    }

    /*
     * Contagem de Músicas por Playlist: Liste o nome de todas as Playlists e o
     * número total de Músicas que cada uma contém. A listagem deve ser ordenada da
     * Playlist mais longa para a mais curta. (Foco em agregação e manipulação da
     * chave composta da Playlist).
     */
    @Test
    void testPlaylistSizes() {
        Set<Pair<String, Long>> playlistNamesAndSizes = playlistRepository.getPlaylistsNamesAndSizes();
        assertThat(playlistNamesAndSizes).isNotEmpty();

        for (Pair<String, Long> par : playlistNamesAndSizes) {
            PlaylistModel playlist = playlistRepository.findByNome(par.getFirst()).orElseThrow();

            assertThat(playlist.getNome()).isEqualTo(par.getFirst());
            assertThat(playlist.getMusicas()).hasSize(par.getSecond().intValue());
        }
    }

    /*
     * Artistas Sem Músicas em Playlists: Identifique e liste todos os Artistas que
     * não possuem nenhuma de suas Músicas adicionadas a nenhuma Playlist no
     * sistema. (Foco em operadores NOT IN, LEFT JOIN ou EXCEPT).
     */
    @Test
    void artistasWithNoMusicsOnPlaylists() {
        createNewArtista();

        Set<ArtistaModel> artistas = artistaRepository.artistasSemMusicasEmQualquerPlaylist();
        assertThat(artistas).hasSizeGreaterThanOrEqualTo(1); // um artista foi adicionado

        for (ArtistaModel artista : artistas) {
            Set<MusicaModel> musicasSemPlaylist = artista.getMusicas();

            assertThat(musicasSemPlaylist).extracting(MusicaModel::getPlaylists)
                    .allMatch(playlists -> playlists.isEmpty());
        }
    }

}
