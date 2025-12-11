package com.example.demo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dtos.artista.ArtistaPlaylistsDTO;
import com.example.demo.models.ArtistaModel;

@Repository
public interface ArtistaRepository extends RepositoryInterface <ArtistaModel, Long> {
    public Optional<ArtistaModel> findByNome(String nome);

    /*
        select
            am1_0.id,
            am1_0.nacionalidade,
            am1_0.nome
        from
            ARTISTA am1_0
        where
            not exists(select
                m1_0.id
            from
                MUSICA m1_0
            join
                MUSICA_PLAYLIST p1_0
                    on m1_0.id=p1_0.musica_id
            where
                am1_0.id=m1_0.artista_id)
    */

    @Query("SELECT a FROM ArtistaModel a "
            + "WHERE NOT EXISTS ("
            + "   SELECT m FROM a.musicas m "
            + "   JOIN m.playlists mp"
            + ")")
    public Set<ArtistaModel> artistasSemMusicasEmQualquerPlaylist();




    /*
        select
            am1_0.id,
            am1_0.nacionalidade,
            am1_0.nome,
            coalesce(count(p1_0.playlist_id), 0)
        from
            ARTISTA am1_0
        left join
            MUSICA m1_0
                on am1_0.id=m1_0.artista_id
        left join
            MUSICA_PLAYLIST p1_0
                on m1_0.id=p1_0.musica_id
        group by
            am1_0.id
        order by
            count(p1_0.playlist_id) desc
    */
    @Query("SELECT a, COALESCE(count(p.id.playlistId), 0) FROM ArtistaModel a "
            + "LEFT JOIN a.musicas m "
            + "LEFT JOIN m.playlists mp "
            + "LEFT JOIN mp.playlist p "
            + "GROUP BY a "
            + "ORDER BY count(p.id.playlistId) DESC")
    public Set<ArtistaPlaylistsDTO> rankearArtistasPorPlaylists();
}
