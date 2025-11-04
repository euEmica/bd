package com.example.demo.repositories;

import java.util.Optional;

import com.example.demo.models.UsuarioModel;

public interface UsuarioRepository extends RepositoryInterface<UsuarioModel, Long> {
    Optional<UsuarioModel> findByUsername(String nome);
}
