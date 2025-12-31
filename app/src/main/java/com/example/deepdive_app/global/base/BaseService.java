package com.example.deepdive_app.global.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

public interface BaseService<D, E, ID> {

    JpaRepository<E, ID> repository();
    D toDto(E entity);
    E toEntity(D dto);

    @Transactional(readOnly = true)
    default D findById(ID id) {
        return repository().findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional(readOnly = true)
    default List<D> findAll() {
        return repository().findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    default D save(D dto) {
        E entity = toEntity(dto);
        E saved = repository().save(entity);
        return toDto(saved);
    }

    @Transactional
    default void delete(ID id) {
        if (!repository().existsById(id)) {
            throw new EntityNotFoundException();
        }
        repository().deleteById(id);
    }
}