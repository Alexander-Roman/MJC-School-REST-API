package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    List<Tag> findAll();

    Tag save(Tag tag);

    void delete(Tag tag);

}
