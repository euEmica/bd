package com.example.demo.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "USUARIO")
public class UsuarioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id_seq")
    @SequenceGenerator(name = "usuario_id_seq",  sequenceName = "usuario_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, name="username")
    private String username;
    private String email;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Set<PlaylistModel> playlists = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<PlaylistModel> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<PlaylistModel> playlists) {
        this.playlists = playlists;
    }

}
