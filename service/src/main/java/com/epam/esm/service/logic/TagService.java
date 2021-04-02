package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {

    List<Long> createIfNotExist(Set<Tag> tags);

}
