package com.example.demo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.example.demo.models.UsuarioModel;

public interface UsuarioRepository extends RepositoryInterface<UsuarioModel, Long> {
    /* */
    Optional<UsuarioModel> findByUsername(String nome);


    /*
        select
            distinct um1_0.username
        from
            USUARIO um1_0
        join
            PLAYLIST p1_0
                on um1_0.id=p1_0.usuario_id
        join
            MUSICA_PLAYLIST m1_0
                on p1_0.playlist_id=m1_0.playlist_id
                and p1_0.usuario_id=m1_0.usuario_id
        join
            MUSICA m2_0
                on m2_0.id=m1_0.musica_id
        where
            m2_0.titulo=?

    */
    @Query("SELECT DISTINCT u.username FROM UsuarioModel u "
        + " INNER JOIN u.playlists p "
        + " INNER JOIN p.musicas pm "
        + " INNER JOIN pm.musica m "
        + " WHERE m.titulo = :musicTitle ")
    public Set<String> findUsernameWithMusicInPlaylists(String musicTitle);
}
