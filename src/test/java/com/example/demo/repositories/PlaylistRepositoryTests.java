package com.example.demo.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

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

    UsuarioModel usuario;

    @BeforeEach
    void setUp() {
        usuario = usuarioRepository.findByUsername("Alexandre").orElseThrow();
    }

    @Test
    // @Rollback(false) // remova o comentário para manter as alterações após o teste
    void testSavePlaylist() {
        PlaylistModel playlist = new PlaylistModel();

        playlist.setNome("Clássicos do Alexandre");
        playlist.setDataCriacao(null); // default current timestamp
        playlist.setUsuario(usuario);
        playlist = playlistRepository.save(playlist);

        assertThat(playlist.getId()).isNotNull();
        assertThat(playlist.getId().getUsuarioId()).isEqualTo(usuario.getId());
    }

}
