package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagDao {

    Optional<Tag> findById(Long id);

    List<Tag> findAll();

    Tag create(Tag tag);

    void createIfNotExists(Set<Tag> tags);

    void delete(Long id);

    void deleteUnused();

}
