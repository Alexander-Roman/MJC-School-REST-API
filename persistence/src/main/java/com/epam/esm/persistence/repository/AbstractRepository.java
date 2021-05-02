package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Identifiable;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Transactional
public class AbstractRepository<T extends Identifiable> implements Repository<T> {

    private final Class<T> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AbstractRepository() {
        entityClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractRepository.class);
    }

    @Override
    public Optional<T> findById(Long id) {
        T entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<T> findSingle(Specification<T> specification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> entityRoot = criteriaQuery.from(entityClass);
        criteriaQuery = criteriaQuery.select(entityRoot).distinct(true);

        Predicate predicate = specification.toPredicate(entityRoot, criteriaQuery, criteriaBuilder);
        criteriaQuery = criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).setMaxResults(1).getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public Page<T> find(Pageable pageable, Specification<T> specification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> entityRoot = criteriaQuery.from(entityClass);
        criteriaQuery = criteriaQuery.select(entityRoot).distinct(true);

        Predicate predicate = specification.toPredicate(entityRoot, criteriaQuery, criteriaBuilder);
        criteriaQuery = criteriaQuery.where(predicate);

        Sort sort = pageable.getSort();
        List<Order> orderList = QueryUtils.toOrders(sort, entityRoot, criteriaBuilder);
        criteriaQuery = criteriaQuery.orderBy(orderList);

        int offset = (int) pageable.getOffset();
        int pageSize = pageable.getPageSize();
        List<T> resultList = entityManager
                .createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> entityCountRoot = countQuery.from(entityClass);
        Expression<Long> countExpression = criteriaBuilder.count(entityCountRoot);
        countQuery = countQuery.select(countExpression);

        Predicate countPredicate = specification.toPredicate(entityCountRoot, countQuery, criteriaBuilder);
        countQuery = countQuery.where(countPredicate);

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, count);
    }

    @Override
    public T save(T entity) {
        if (entity.getId() != null) {
            return entityManager.merge(entity);
        }
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        T entity = entityManager.find(entityClass, id);
        entityManager.remove(entity);
    }

    @Override
    public Optional<T> findSingleByNativeQuery(String sql) {
        Query nativeQuery = entityManager.createNativeQuery(sql, entityClass);
        Object singleResult = nativeQuery.getSingleResult();
        T entity = entityClass.cast(singleResult);
        return Optional.ofNullable(entity);
    }

}
