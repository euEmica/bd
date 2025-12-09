package com.example.demo.repositories;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface RepositoryInterface<T, ID extends Serializable>
    extends Repository<T, ID> {

    <S extends T> S save(S entity);

    <S extends T> Set<S> saveAll(Iterable<S> entities);

    Optional<T> findById(ID id);

    Set<T> findAll();

    void deleteById(ID id);
}
