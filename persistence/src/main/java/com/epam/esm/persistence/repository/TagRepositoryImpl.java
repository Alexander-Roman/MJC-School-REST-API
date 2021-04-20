package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.Tag_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> findById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }

    @Override
    public Optional<Tag> findByName(String searchName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery = criteriaQuery.select(tagRoot);

        String searchNameInLowerCase = searchName.toLowerCase();
        Path<String> namePath = tagRoot.get(Tag_.name);
        Expression<String> nameToLowerCase = criteriaBuilder.lower(namePath);
        Predicate predicate = criteriaBuilder.equal(nameToLowerCase, searchNameInLowerCase);
        criteriaQuery = criteriaQuery.where(predicate);

        List<Tag> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return resultList
                .stream()
                .findFirst();
    }

    @Override
    public List<Tag> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery = criteriaQuery.select(tagRoot);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Tag save(Tag tag) {
        String name = tag.getName();
        String nameInLowerCase = name.toLowerCase();
        Tag formalized = Tag.Builder
                .from(tag)
                .setName(nameInLowerCase)
                .build();
        if (tag.getId() == null) {
            entityManager.persist(formalized);
        } else {
            entityManager.merge(formalized);
        }
        return formalized;
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }

    @Override
    public void deleteUnused() {

    }

}
