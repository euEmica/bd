package com.example.demo.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.models.ArtistaModel;
import com.example.demo.models.MusicaModel;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // para garantir que vai usar o postgres
@Rollback(false)
public class MusicaRepositoryTests {

    @Autowired
    private MusicaRepository musicaRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    private ArtistaModel queen;

    @BeforeEach
    void setUp() {
        queen = artistaRepository.findById(1L).orElseThrow();
    }

    private MusicaModel criaMusicaModel() {
        MusicaModel musica = new MusicaModel();
        musica.setTitulo("The Show Must Go On");
        musica.setDuracaoSegundos(270);
        musica.setArtista(queen);
        return musica;
    }

    private MusicaModel findById(Long id) {
        return musicaRepository.findById(id).orElse(null);
    }

    @Test
    // @Rollback(false) // remova o comentário para manter as alterações após o
    // teste
    void testSaveMusica() {

        MusicaModel musica = criaMusicaModel();

        musicaRepository.save(musica);

        assertThat(musica.getId()).isNotNull();

        MusicaModel foundMusica = findById(musica.getId());

        assertThat(foundMusica).isNotNull();
        assertThat(foundMusica.getTitulo()).isEqualTo("The Show Must Go On");
        assertThat(foundMusica.getDuracaoSegundos()).isEqualTo(270);
        assertThat(foundMusica.getArtista()).isEqualTo(queen);

    }

    @Test
    void testFindMusicaById() {

        MusicaModel musica = criaMusicaModel();
        musicaRepository.save(musica);

        MusicaModel foundMusica = findById(musica.getId());
        assertThat(foundMusica).isNotNull();
        assertThat(foundMusica.getTitulo()).isEqualTo("The Show Must Go On");
        assertThat(foundMusica.getDuracaoSegundos()).isEqualTo(270);
    }

    @Test
    void testUpdateMusica() {

        MusicaModel musica = criaMusicaModel();
        musica = musicaRepository.save(musica);

        assertThat(musica).isNotNull();
        assertThat(musica.getId()).isNotNull();

        musica.setDuracaoSegundos(280);
        musicaRepository.save(musica);

        MusicaModel updatedMusica = findById(musica.getId());
        assertThat(updatedMusica).isNotNull();
        assertThat(updatedMusica.getDuracaoSegundos()).isEqualTo(280);
    }

    @Test
    // @Rollback(false) // remova o comentário para manter as alterações após o
    // teste
    void testDeleteMusica() {

        MusicaModel musica = criaMusicaModel();
        musica = musicaRepository.save(musica);

        assertThat(musica).isNotNull();
        assertThat(musica.getId()).isNotNull();

        musicaRepository.deleteById(musica.getId());

        MusicaModel deletedMusica = findById(musica.getId());
        assertThat(deletedMusica).isNull();
    }

}
