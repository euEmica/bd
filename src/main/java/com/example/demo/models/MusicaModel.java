package com.example.demo.models;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ConditionalOnIssuerLocationJwtDecoder;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "`MUSICA`")
public class MusicaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne()
    @JoinColumn(name = "artista_id")
    private ArtistaModel artista;

    @Column(nullable = false, unique = true)
    private String titulo;
    @Column(nullable = false)
    private Integer duracao;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ArtistaModel getArtista() {
        return artista;
    }

    public void setArtista(ArtistaModel artista) {
        this.artista = artista;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

}
