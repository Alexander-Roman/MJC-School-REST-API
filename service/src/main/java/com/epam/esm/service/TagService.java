package com.epam.esm.service;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Set;

/**
 * Business logic interface for Tags
 */
public interface TagService {

    /**
     * Finds tag by specified id
     *
     * @param id of tag to find
     * @return Tag found
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     * @throws EntityNotFoundException  when found noting
     */
    Tag findById(Long id);

    /**
     * Finds and returns list of all Tags
     *
     * @return List of all Tags
     */
    List<Tag> findAll();

    /**
     * Creates new Tag
     *
     * @param tag to create
     * @return Tag created in effect
     * @throws ServiceException when tag invalid
     */
    Tag create(Tag tag);

    /**
     * Manages several tags to find each by name and create new one if found nothing
     * This method returns all tags with id, either found or created
     *
     * @param tags to manage
     * @return Set of all found and created tags
     * @throws NullPointerException when tags parameter is null
     * @throws ServiceException     when one of tags invalid
     */
    Set<Tag> createIfNotExist(Set<Tag> tags);

    /**
     * Deletes tag by specified id
     *
     * @param id of tag being deleted
     * @return Tag deleted in effect
     * @throws NullPointerException     when id is null
     * @throws IllegalArgumentException when id is not valid
     * @throws EntityNotFoundException  when deleting tag not found
     */
    Tag deleteById(Long id);

    /**
     * Deletes all Tags not attached to any certificate
     */
    void deleteUnused();

}
