package com.example.demo.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.MusicaModel;

@Repository
public interface MusicaRepository extends RepositoryInterface<MusicaModel, Long> {
    /*
        select
            distinct mm1_0.id,
            mm1_0.artista_id,
            mm1_0.duracao_segundos,
            mm1_0.titulo
        from
            MUSICA mm1_0
        join
            MUSICA_PLAYLIST p1_0
                on mm1_0.id=p1_0.musica_id
        join
            PLAYLIST p2_0
                on p2_0.playlist_id=p1_0.playlist_id
                and p2_0.usuario_id=p1_0.usuario_id
        join
            USUARIO u1_0
                on u1_0.id=p2_0.usuario_id
                and u1_0.username=?
        join
            ARTISTA a1_0
                on a1_0.id=mm1_0.artista_id
                and a1_0.nome=?
    */
    @Query("SELECT DISTINCT m FROM MusicaModel m "
            + "INNER JOIN m.playlists mp "
            + "INNER JOIN mp.playlist p "
            + "INNER JOIN p.usuario u on u.username = :usuario "
            + "INNER JOIN m.artista a on a.nome = :artista ")
    public Set<MusicaModel> musicasDeUmArtistaEmPlaylistDeUsuario(String artista, String usuario);

    public Optional<MusicaModel> findByTitulo(String titulo);


    /*
        select
            mm1_0.id,
            mm1_0.artista_id,
            mm1_0.duracao_segundos,
            mm1_0.titulo,
            a1_0.id,
            a1_0.nacionalidade,
            a1_0.nome
        from
            MUSICA mm1_0
        join
            ARTISTA a1_0
                on a1_0.id=mm1_0.artista_id
        where
            mm1_0.id=?
    */

    @Query("SELECT m, a FROM MusicaModel m "
        + "INNER JOIN m.artista a "
        + "WHERE m.id = :id")
    public Optional<MusicaModel> findByIdWithArtista(Long id);



    /*
        select
            mm1_0.id,
            mm1_0.artista_id,
            mm1_0.duracao_segundos,
            mm1_0.titulo
        from
            MUSICA mm1_0
        where
            mm1_0.duracao_segundos<(
                select
                    avg(mm2_0.duracao_segundos)
                from
                    MUSICA mm2_0
                where
                    mm2_0.artista_id=mm1_0.artista_id
            )
    */

    @Query("SELECT m FROM MusicaModel m"
        + " WHERE m.duracaoSegundos < "
        + " (SELECT AVG(m2.duracaoSegundos) FROM MusicaModel m2 WHERE m2.artista.id = m.artista.id)")
    public Set<MusicaModel> findMusicasMaisCurtasQueAMedia();
}
