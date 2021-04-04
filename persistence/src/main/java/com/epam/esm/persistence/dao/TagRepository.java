package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.specification.Specification;

import java.util.List;
import java.util.Set;

public interface TagRepository {

    List<Tag> find(Specification<Tag> specification);

    Tag create(Tag tag);

    List<Long> saveIfNotExist(Set<Tag> tags);

    void delete(Long id);

    void deleteUnused();

}
