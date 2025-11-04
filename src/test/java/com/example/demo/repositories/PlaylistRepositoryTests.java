package com.example.demo.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.models.MusicaModel;
import com.example.demo.models.PlaylistModel;
import com.example.demo.models.UsuarioModel;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // para garantir que vai usar o postgres
public class PlaylistRepositoryTests {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MusicaRepository musicaRepository;

    UsuarioModel usuario;

    @BeforeEach
    void setUp() {
        usuario = usuarioRepository.findByUsername("Alexandre").orElseThrow();
    }

    PlaylistModel criaPlaylistModel() {
        PlaylistModel playlist = new PlaylistModel();
        playlist.setNome("Clássicos do Alexandre");
        playlist.setDataCriacao(null); // default current timestamp
        playlist.setUsuario(usuario);
        return playlist;
    }

    @Test
    // @Rollback(false) // remova o comentário para manter as alterações após o
    // teste
    void testSavePlaylist() {
        PlaylistModel playlist = criaPlaylistModel();
        playlist = playlistRepository.save(playlist);

        assertThat(playlist.getId()).isNotNull();
        assertThat(playlist.getId().getUsuarioId()).isEqualTo(usuario.getId());
    }

    @Test
    // @Rollback(false)
    void testAddMusicasToPlaylist() {
        PlaylistModel playlist = playlistRepository.save(criaPlaylistModel());

        MusicaModel bohemianRhapsody = musicaRepository.findById(1L).orElseThrow();
        MusicaModel stairwayToHeaven = musicaRepository.findById(2L).orElseThrow();

        playlist.addMusica(bohemianRhapsody);
        playlist.addMusica(stairwayToHeaven);

        playlist = playlistRepository.save(playlist);

        PlaylistModel foundPlaylist = playlistRepository.findById(playlist.getId()).orElseThrow();
        assertThat(foundPlaylist.getMusicas()).hasSize(2);
        assertThat(foundPlaylist.getMusicas()).extracting("musica.id")
                .containsExactlyInAnyOrder(bohemianRhapsody.getId(), stairwayToHeaven.getId());
    }

    @Test
    void testRemoveMusicaFromPlaylist() {
        PlaylistModel playlist = playlistRepository.save(criaPlaylistModel());

        MusicaModel bohemianRhapsody = musicaRepository.findById(1L).orElseThrow();
        MusicaModel stairwayToHeaven = musicaRepository.findById(2L).orElseThrow();

        playlist.addMusica(bohemianRhapsody);
        playlist.addMusica(stairwayToHeaven);

        playlist = playlistRepository.save(playlist);

        // Remover uma música
        playlist.removeMusica(bohemianRhapsody);
        playlist = playlistRepository.save(playlist);

        PlaylistModel foundPlaylist = playlistRepository.findById(playlist.getId()).orElseThrow();
        assertThat(foundPlaylist.getMusicas()).hasSize(1);
        assertThat(foundPlaylist.getMusicas()).extracting("musica.id").containsExactly(stairwayToHeaven.getId());
    }

}
