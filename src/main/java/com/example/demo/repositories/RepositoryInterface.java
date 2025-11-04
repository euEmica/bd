package com.example.demo.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface RepositoryInterface<T, ID extends Serializable>
    extends Repository<T, ID> {

    <S extends T> S save(S entity);

    Optional<T> findById(ID id);

    void deleteById(ID id);
}
