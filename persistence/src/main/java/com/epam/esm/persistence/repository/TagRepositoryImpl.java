package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class TagRepositoryImpl extends AbstractRepository<Tag> implements TagRepository {

}
