package com.example.demo.queries;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.models.*;
import com.example.demo.models.Ids.PlaylistID;
import com.example.demo.repositories.*;

import jakarta.transaction.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class Teste2_5TransacaoEConcorrencia {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicaPlaylistRepository musicaPlaylistRepository;

    @Test
    @Transactional
    @Rollback(false)
    void testeTransacaoMusicaPlaylist() {

        UsuarioModel usuario = usuarioRepository.findById(1L).orElseThrow();
        PlaylistModel playlist0 = playlistRepository.findByIdPlaylistIdAndIdUsuarioId(1L, usuario.getId())
                .orElseThrow();
        PlaylistModel playlist1 = playlistRepository.findByIdPlaylistIdAndIdUsuarioId(3L, usuario.getId())
                .orElseThrow();

        System.out.println(playlist0);
        System.out.println(playlist1);
        System.out.println(playlist0.getMusicas().containsAll(playlist1.getMusicas()));

        if (playlist0.getMusicas().containsAll(playlist1.getMusicas())
                && playlist0.getMusicas().size() == playlist1.getMusicas().size()) {
            System.out
                    .println("As músicas da playlist '" + playlist1.getNome() + "' já estão todas na playlist '"
                            + playlist0.getNome() + "'. Nada a fazer.");
            return;
        }

        PlaylistModel from = playlist0.getMusicas().containsAll(playlist1.getMusicas()) ? playlist0 : playlist1;
        PlaylistModel to = from == playlist0 ? playlist1 : playlist0;

        Set<MusicaPlaylistModel> diff = new HashSet<>(from.getMusicas());
        diff.removeAll(to.getMusicas());

        System.out.println(diff);

        MusicaPlaylistModel musicaPlaylistModel = diff.iterator().next();
        MusicaModel musicaToChange = musicaPlaylistModel.getMusica();
        System.out.println(musicaToChange);

        to.addMusica(musicaToChange);

        from.removeMusica(musicaToChange);

        playlistRepository.save(to);
        playlistRepository.save(from);

        System.out.println(usuario);
        System.out.println(from);
        System.out.println(to);
    }

}
