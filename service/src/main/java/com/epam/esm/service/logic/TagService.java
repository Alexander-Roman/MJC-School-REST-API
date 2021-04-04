package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService {

    List<Long> createIfNotExist(Set<Tag> tags);

    Optional<Tag> findById(Long id);

    List<Tag> findAll();

    Tag create(Tag tag);

    void deleteById(Long id);

    void deleteUnused();

}
