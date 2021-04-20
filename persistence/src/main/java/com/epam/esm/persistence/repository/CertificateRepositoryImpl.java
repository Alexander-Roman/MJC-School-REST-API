package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Certificate_;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.Tag_;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.Sort;
import com.epam.esm.persistence.model.SortRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CertificateRepositoryImpl implements CertificateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Certificate> findById(Long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        return Optional.ofNullable(certificate);
    }

    @Override
    public List<Certificate> findAll(SortRequest sortRequest, FilterRequest filterRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> certificateRoot = criteriaQuery.from(Certificate.class);
        certificateRoot.fetch(Certificate_.tags, JoinType.LEFT);
        criteriaQuery = criteriaQuery.select(certificateRoot).distinct(true);
        criteriaQuery = this.buildFilterCriteria(criteriaQuery, certificateRoot, filterRequest);
        criteriaQuery = this.buildSortCriteria(criteriaQuery, certificateRoot, sortRequest);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private CriteriaQuery<Certificate> buildFilterCriteria(CriteriaQuery<Certificate> criteriaQuery,
                                                           Root<Certificate> certificateRoot,
                                                           FilterRequest filterRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate criteriaPredicate = criteriaBuilder.conjunction();

        String search = filterRequest.getSearch();
        if (search != null) {
            String searchInLowerCase = search.toLowerCase();

            Path<String> namePath = certificateRoot.get(Certificate_.name);
            Expression<String> nameToLowerCase = criteriaBuilder.lower(namePath);
            Predicate nameMatches = criteriaBuilder.like(nameToLowerCase, "%" + searchInLowerCase + "%");

            Path<String> descriptionPath = certificateRoot.get(Certificate_.description);
            Expression<String> descriptionToLowerCase = criteriaBuilder.lower(descriptionPath);
            Predicate descriptionMatches = criteriaBuilder.like(descriptionToLowerCase, "%" + searchInLowerCase + "%");

            Predicate conjunction = criteriaBuilder.or(nameMatches, descriptionMatches);
            criteriaPredicate = criteriaBuilder.and(criteriaPredicate, conjunction);
        }

        String tagName = filterRequest.getTagName();
        if (tagName != null) {
            SetJoin<Certificate, Tag> tagsJoin = certificateRoot.join(Certificate_.tags, JoinType.LEFT);
            Path<String> tagNamePath = tagsJoin.get(Tag_.name);
            Predicate predicate = criteriaBuilder.equal(tagNamePath, tagName);
            criteriaPredicate = criteriaBuilder.and(criteriaPredicate, predicate);
        }
        return criteriaQuery.where(criteriaPredicate);
    }

    private CriteriaQuery<Certificate> buildSortCriteria(CriteriaQuery<Certificate> criteriaQuery,
                                                         Root<Certificate> certificateRoot,
                                                         SortRequest sortRequest) {
        List<Order> orderList = new ArrayList<>();
        Sort sort = sortRequest.getSort();
        if (sort != null) {
            Order order = this.convert(sort, certificateRoot);
            orderList.add(order);
        }
        Sort thenSort = sortRequest.getThenSort();
        if (thenSort != null) {
            Order order = this.convert(thenSort, certificateRoot);
            orderList.add(order);
        }
        return criteriaQuery.orderBy(orderList);
    }

    private Order convert(Sort sort, Root<Certificate> certificateRoot) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        String field = sort.getField();
        Sort.Direction direction = sort.getDirection();
        switch (direction) {
            case ASC:
                return criteriaBuilder.asc(certificateRoot.get(field));
            case DESC:
                return criteriaBuilder.desc(certificateRoot.get(field));
            default:
                throw new IllegalArgumentException("Unknown sort direction: " + direction);
        }
    }

    @Override
    public Certificate save(Certificate certificate) {
        if (certificate.getId() == null) {
            entityManager.persist(certificate);
        } else {
            entityManager.merge(certificate);
        }
        return certificate;
    }

    @Override
    public void delete(Certificate certificate) {
        entityManager.remove(certificate);
    }

}
