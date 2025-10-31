package com.example.demo.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "`USUARIO`")
public class UsuarioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String userName;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // esse lazy e eager pode modificar como as consultas são feitas(aqui foca na
    // performace(LAZY))
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Set<PlaylistModel> books = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<PlaylistModel> getBooks() {
        return books;
    }

    public void setBooks(Set<PlaylistModel> books) {
        this.books = books;
    }

}
