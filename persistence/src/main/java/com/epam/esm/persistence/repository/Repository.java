package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Identifiable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.Set;

public interface Repository<T extends Identifiable> {

    Optional<T> findById(Long id);

    Optional<T> findSingle(Specification<T> specification);

    Page<T> find(Pageable pageable, Specification<T> specification);

    T save(T entity);

    Set<T> batchSave(Set<T> entities);

    void delete(Long id);

}
