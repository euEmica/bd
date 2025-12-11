package com.example.demo.models;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "MUSICA")
public class MusicaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "musica_id_seq")
    @SequenceGenerator(name = "musica_id_seq",  sequenceName = "musica_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titulo;

    @Column(nullable = false, name="duracao_segundos", columnDefinition = "int4 CHECK ((duracao_segundos > 0))")
    private Integer duracaoSegundos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="artista_id", nullable=false)
    private ArtistaModel artista;

    @OneToMany(mappedBy = "musica")
    private Set<MusicaPlaylistModel> playlists;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public void setDuracaoSegundos(Integer duracao) {
        this.duracaoSegundos = duracao;
    }

    public ArtistaModel getArtista() {
        return artista;
    }

    public void setArtista(ArtistaModel artista) {
        this.artista = artista;
    }

    public Set<MusicaPlaylistModel> getPlaylists() {
        return playlists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicaModel that = (MusicaModel) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public String toString() {
        return "MusicaModel{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", duracaoSegundos=" + duracaoSegundos +
                ", artista=" + (artista != null ? artista.getNome() : "null") +
                '}';
    }
}
