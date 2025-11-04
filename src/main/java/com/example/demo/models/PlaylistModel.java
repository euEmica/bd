package com.example.demo.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.demo.models.Ids.PlaylistID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "PLAYLIST")
public class PlaylistModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public PlaylistModel() {
        this.id = new PlaylistID();
    }

    @EmbeddedId
    private PlaylistID id;

    @CreationTimestamp
    @Column(name="data_criacao")
    private LocalDateTime dataCriacao;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name="usuario_id", nullable=false)
    private UsuarioModel usuario;


    public void setId(PlaylistID id) {
        this.id = id;
    }

    public PlaylistID getId() {
        return id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PlaylistModel other = (PlaylistModel) obj;
        return id != null && id.equals(other.id);
    }

}
