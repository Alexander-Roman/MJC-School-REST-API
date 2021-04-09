package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Common DAO pattern interface for Tag entity
 */
public interface TagDao {

    /***
     * Finds and return Tag by id specified
     *
     * @param id of tag to find
     * @return Optional of Tag found or empty if found nothing
     */
    Optional<Tag> findById(Long id);

    /**
     * Finds and return Tag by name specified
     *
     * @param name of tag to find
     * @return Optional of Tag found or empty if found nothing
     */
    Optional<Tag> findByName(String name);

    /**
     * Finds and returns all Tags
     *
     * @return List of all Tags
     */
    List<Tag> findAll();

    /**
     * Creates new Tag
     *
     * @param tag to create
     * @return Tag created including auto generated id
     */
    Tag create(Tag tag);

    /**
     * Deletes Tag by specified id
     *
     * @param id of Tag to delete
     */
    void delete(Long id);

    /**
     * Deletes all Tags not attached to any certificate
     */
    void deleteUnused();

}
