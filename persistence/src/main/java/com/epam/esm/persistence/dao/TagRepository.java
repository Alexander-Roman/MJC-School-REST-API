package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Set;

public interface TagRepository {

    List<Long> saveIfNotExist(Set<Tag> tags);

}
