package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    List<Tag> findAll();

    Tag create(Tag tag);

    void delete(Long id);

    void deleteUnused();

}
