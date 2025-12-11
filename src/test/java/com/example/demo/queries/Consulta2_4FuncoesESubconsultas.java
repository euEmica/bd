package com.example.demo.queries;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.dtos.artista.ArtistaPlaylistsDTO;
import com.example.demo.models.*;
import com.example.demo.repositories.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @Rollback(false)
public class Consulta2_4FuncoesESubconsultas {

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    /*
     * Rank de Popularidade do Artista: Liste todos os Artistas e seu ranking
     * baseado no número de Playlists em que suas músicas estão presentes (o Artista
     * com músicas na maior quantidade de playlists fica em 1º).
     */
    @Test
    void ranquearArtistas() {
        Set<ArtistaPlaylistsDTO> rankings = artistaRepository.rankearArtistasPorPlaylists();

        for (ArtistaPlaylistsDTO dto : rankings) {
            System.out.println(dto);
        }
    }

    /*
     * Comparação com o Top 1 (Desafio de Subconsulta Correlacionada): Liste todas
     * as Músicas do Artista 'Led Zeppelin' cuja duração é maior que a duração da
     * música mais longa do Artista 'Queen'.
     *
     * O Desafio do ORM: Força o uso de subconsultas complexas (WHERE
     * duracao_segundos > (SELECT MAX(...) FROM MUSICA WHERE artista_id = X)).
     * Muitos ORMs têm sintaxe pobre ou geram SQL ineficiente para subconsultas não
     * correlacionadas.
     *
     */
    @Test
    void musicasMaioresQueAMaiorDoOutroArtista() {
        String artista1 = "Queen";
        String artista2 = "AC/DC";

        Set<MusicaModel> musicas = musicaRepository
                .findMusicasFromArtistaThatIsBiggerThanTheBiggestMusicaFromOtherArtista(artista1, artista2);

        for (MusicaModel musica : musicas) {
            System.out.println(musica);
        }
    }

}
