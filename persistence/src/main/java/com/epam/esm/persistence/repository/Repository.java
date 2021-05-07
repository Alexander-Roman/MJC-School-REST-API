package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Identifiable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

/**
 * Common repository interface for persistent entities
 *
 * @param <T> particular entity type
 */
public interface Repository<T extends Identifiable> {

    /**
     * Finds entity by ID
     *
     * @param id Long value
     * @return Optional of entity found, or empty if found nothing
     */
    Optional<T> findById(Long id);

    /**
     * Finds single entity by ID
     *
     * @param specification of particular entity type
     * @return Optional of entity found, or empty if found nothing
     */
    Optional<T> findSingle(Specification<T> specification);

    /**
     * Finds page of entities by specification and given page number, page size, sorting properties.
     *
     * @param pageable      object containing sorting and pagination parameters
     * @param specification of particular entity type
     * @return Page containing a list of entities corresponding to the specified parameters,
     * the total number of available entities, and pagination parameters themselves
     */
    Page<T> find(Pageable pageable, Specification<T> specification);

    /**
     * Saves entity. Creates new if ID is not specified, otherwise updates existing one
     *
     * @param entity to save
     * @return entity in a saved state
     */
    T save(T entity);

    /**
     * Finds and deletes entity by ID
     *
     * @param id id Long value
     */
    void delete(Long id);

    /**
     * Finds single entity using plain SQL query
     *
     * @param sql query to perform
     * @return Optional of entity found, or empty if found nothing
     */
    Optional<T> findSingleByNativeQuery(String sql);

}
