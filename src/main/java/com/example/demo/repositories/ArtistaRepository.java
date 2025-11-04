package com.example.demo.repositories;

import org.springframework.stereotype.Repository;

import com.example.demo.models.ArtistaModel;

@Repository
interface ArtistaRepository extends RepositoryInterface <ArtistaModel, Long> {
}
