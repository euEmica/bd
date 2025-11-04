package com.example.demo.repositories;

import org.springframework.stereotype.Repository;

import com.example.demo.models.MusicaModel;

@Repository
interface MusicaRepository extends RepositoryInterface<MusicaModel, Long> {
}
