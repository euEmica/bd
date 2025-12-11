package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.playlists.PlaylistDurationDTO;
import com.example.demo.models.PlaylistModel;
import com.example.demo.models.Ids.PlaylistID;

@Repository
public interface PlaylistRepository extends RepositoryInterface<PlaylistModel, PlaylistID> {

    /*
    select
        pm1_0.playlist_id,
        pm1_0.usuario_id,
        pm1_0.data_criacao,
        pm1_0.nome
    from
        PLAYLIST pm1_0
    join
        USUARIO u1_0
            on u1_0.id=pm1_0.usuario_id
    where
        u1_0.username=?
    */
    public Set<PlaylistModel> findByUsuarioUsername(String username);

    /*
        select
            pm1_0.nome,
            count(*)
        from
            PLAYLIST pm1_0
        join
            MUSICA_PLAYLIST m1_0
                on pm1_0.playlist_id=m1_0.playlist_id
                and pm1_0.usuario_id=m1_0.usuario_id
        group by
            pm1_0.playlist_id,
            pm1_0.usuario_id
        order by
            pm1_0.nome desc
    */
    @Query("SELECT p.nome, count(pm) FROM PlaylistModel p "
            + "INNER JOIN p.musicas pm "
            + "GROUP BY p.id "
            + "ORDER BY p.nome DESC")
    public List<Pair<String, Long>> getPlaylistsNamesAndSizes();

    public Optional<PlaylistModel> findByNome(String nome);


    /*
        select
            pm1_0.nome,
            u1_0.username,
            coalesce(sum(m2_0.duracao_segundos), 0)
        from
            PLAYLIST pm1_0
        join
            USUARIO u1_0
                on u1_0.id=pm1_0.usuario_id
        left join
            MUSICA_PLAYLIST m1_0
                on pm1_0.playlist_id=m1_0.playlist_id
                and pm1_0.usuario_id=m1_0.usuario_id
        left join
            MUSICA m2_0
                on m2_0.id=m1_0.musica_id
        group by
            pm1_0.nome,
            u1_0.username
    */

    @Query("SELECT p.nome, u.username, COALESCE(SUM(m.duracaoSegundos), 0) "
            + "FROM PlaylistModel p "
            + "INNER JOIN p.usuario u "
            + "LEFT JOIN p.musicas pm "
            + "LEFT JOIN pm.musica m "
            + "GROUP BY p.nome, u.username")
    public Set<PlaylistDurationDTO> getPlaylistDurations();


    /*
        select
            m2_0.titulo,
            m1_0.ordem_na_playlist
        from
            PLAYLIST pm1_0
        join
            MUSICA_PLAYLIST m1_0
                on pm1_0.playlist_id=m1_0.playlist_id
                and pm1_0.usuario_id=m1_0.usuario_id
        join
            MUSICA m2_0
                on m2_0.id=m1_0.musica_id
        where
            pm1_0.nome=?
        order by
            m1_0.ordem_na_playlist
    */

    @Query("SELECT m.titulo, pm.ordem"
        + " FROM PlaylistModel p"
        + " INNER JOIN p.musicas pm"
        + " INNER JOIN pm.musica m"
        + " WHERE p.nome = :playlistNome"
        + " ORDER BY pm.ordem ASC")
    public List<Pair<String, Long>> getPlaylistsMusicasAndOrder(String playlistNome);
}
