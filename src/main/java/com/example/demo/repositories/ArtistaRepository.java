package com.example.demo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
        join
            MUSICA m1_0
                on am1_0.id=m1_0.artista_id
        where
            0=(
                select
                    count(*)
                from
                    MUSICA_PLAYLIST p1_0
                where
                    m1_0.id=p1_0.musica_id
            )
    */
    @Query("SELECT a FROM ArtistaModel a "
           + "INNER JOIN a.musicas m "
           + "WHERE 0 = (SELECT count(*) FROM m.playlists)")
    public Set<ArtistaModel> artistasSemMusicasEmQualquerPlaylist();
}
