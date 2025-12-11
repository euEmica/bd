package com.example.demo.queries;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.dtos.playlists.PlaylistDurationDTO;
import com.example.demo.models.*;
import com.example.demo.repositories.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @Rollback(false)
public class Consulta2_2OtimizacaoEDetalheTests {
    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    /*
     * Detalhes Completos da Música com Artista: Crie uma função para buscar uma
     * Música por seu id e, em uma única operação de consulta (evitando o problema
     * N+1), carregue (fetch) automaticamente todos os detalhes do Artista
     * relacionado. (Foco em Eager Loading ou Fetching Join).
     */
    @Test
    void getMusicAndArtist() {
        ArtistaModel queen = artistaRepository.findByNome("Queen").orElseThrow();

        MusicaModel novaMusica = new MusicaModel();
        novaMusica.setTitulo("Nova Música");
        novaMusica.setDuracaoSegundos(233);
        novaMusica.setArtista(queen);

        novaMusica = musicaRepository.save(novaMusica);

        MusicaModel foundMusica = musicaRepository.findByIdWithArtista(novaMusica.getId()).orElseThrow();

        assertThat(foundMusica).isNotNull();

        assertThat(foundMusica.getArtista()).isNotNull();
        assertThat(foundMusica.getArtista().getNome()).isEqualTo("Queen");

    }

    /*
     * Tempo Total de Reprodução da Playlist: Para cada PLAYLIST no sistema, calcule
     * e retorne o tempo total de reprodução (soma de duracao_segundos de todas as
     * músicas). A saída deve listar o nome da Playlist, o username do Dono e o
     * tempo total de reprodução. (Foco em agregação SUM e GROUP BY sobre o N:N).
     */
    @Test
    void getPlaylistsDurations() {
        Set<PlaylistDurationDTO> durations = playlistRepository.getPlaylistDurations();

        for (PlaylistDurationDTO duration : durations)
            System.out.println(duration);
    }

    /*
     * Músicas Mais Curtas que a Média do Artista: Liste todas as Músicas cujo tempo
     * de duração (duracao_segundos) é menor que o tempo de duração médio de todas
     * as músicas do seu próprio Artista (ex: listar músicas do AC/DC que são mais
     * curtas que a média do AC/DC). (Foco em subconsultas ou Window Functions se o
     * ORM suportar).
     */
    @Test
    void checkMusicasShorterThanArtistAverage() {
        Set<MusicaModel> musicasMenoresQueAMediaDoArtista = musicaRepository.findMusicasMaisCurtasQueAMedia();

        Set<ArtistaModel> artistas = artistaRepository.findAll();
        Map<Long, Double> mediasPorArtista = artistas.stream()
                .collect(Collectors.toMap(
                        ArtistaModel::getId,
                        artista -> artista.getMusicas().stream()
                                .mapToDouble(MusicaModel::getDuracaoSegundos)
                                .average()
                                .orElse(0.0)));

        for (MusicaModel musica : musicasMenoresQueAMediaDoArtista) {
            assertThat(mediasPorArtista).containsKey(musica.getArtista().getId());
            double media = mediasPorArtista.get(musica.getArtista().getId());
            assertThat((double) musica.getDuracaoSegundos()).isLessThan(media);
        }
    }
}
