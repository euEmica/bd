package com.example.demo.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.models.ArtistaModel;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArtistaRepositoryTests {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Test
    // @Rollback(false) // remova o comentário para manter as alterações após o teste
    void testSaveArtista() {
        ArtistaModel artista = new ArtistaModel();
        artista.setNome("Chico Science");
        artista.setNacionalidade("Brasileira");
        artistaRepository.save(artista);

        assertThat(artista.getId()).isNotNull();

        ArtistaModel foundArtista = artistaRepository.findById(artista.getId()).orElse(null);
        assertThat(foundArtista).isNotNull();
        assertThat(foundArtista.getNome()).isEqualTo("Chico Science");
        assertThat(foundArtista.getNacionalidade()).isEqualTo("Brasileira");
    }

    @Test
    void testFindArtistaById() {
        Long artistaId = 1L; // Queen

        ArtistaModel foundArtista = artistaRepository.findById(artistaId).orElse(null);
        assertThat(foundArtista).isNotNull();
        assertThat(foundArtista.getNome()).isEqualTo("Queen");
        assertThat(foundArtista.getNacionalidade()).isEqualTo("Britânica");
    }

    @Test
    void testFindArtistaByIdNotFound() {
        Long artistaId = 999L; // Artista inexistente

        ArtistaModel foundArtista = artistaRepository.findById(artistaId).orElse(null);
        assertThat(foundArtista).isNull();
    }

    @Test
    // @Rollback(false)
    void updateArtista() {
        ArtistaModel artista = new ArtistaModel();
        artista.setNome("Macacos do Ártico");
        artista.setNacionalidade("Britânica");

        artista = artistaRepository.save(artista);

        artista.setNome("Arctic Monkeys");
        artistaRepository.save(artista);

        ArtistaModel updatedArtista = artistaRepository.findById(artista.getId()).orElse(null);
        assertThat(updatedArtista).isNotNull();
        assertThat(updatedArtista.getNome()).isEqualTo("Arctic Monkeys");
        assertThat(updatedArtista.getNacionalidade()).isEqualTo("Britânica");
    }

    @Test
    // @Rollback(false)
    void deleteArtista() {
        Long artistaId = 2L; // Led Zeppelin

        artistaRepository.deleteById(artistaId);

        ArtistaModel deletedArtista = artistaRepository.findById(artistaId).orElse(null);
        assertThat(deletedArtista).isNull();
    }

}
