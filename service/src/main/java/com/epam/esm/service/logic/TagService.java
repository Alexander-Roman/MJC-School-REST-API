package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {

    Tag findById(Long id);

    List<Tag> findAll();

    Tag create(Tag tag);

    Set<Tag> createIfNotExist(Set<Tag> tags);

    Tag deleteById(Long id);

    void deleteUnused();

}
