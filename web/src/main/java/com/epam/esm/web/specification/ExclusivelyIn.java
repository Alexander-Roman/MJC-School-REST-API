package com.epam.esm.web.specification;

import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.PathSpecification;
import net.kaczmarzyk.spring.data.jpa.utils.Converter;
import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Arrays;

public class ExclusivelyIn<T> extends PathSpecification<T> {

    private static final long serialVersionUID = 1L;

    private String[] allowedValues;
    private Converter converter;

    public ExclusivelyIn(QueryContext queryContext, String path, String[] httpParamValues, Converter converter) {
        super(queryContext, path);
        if (httpParamValues == null || httpParamValues.length < 1) {
            throw new IllegalArgumentException();
        }
        this.allowedValues = httpParamValues;
        this.converter = converter;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Path<?> path = path(root);
        Class<?> typeOnPath = path.getJavaType();

        Subquery<T> subQuery = query.subquery((Class<T>) root.getJavaType());
        Root<T> subQueryRoot = subQuery.from((Class<T>) root.getJavaType());
        Path<?> subQueryPath = path(subQueryRoot);
        Predicate predicate = subQueryPath.in(converter.convert(Arrays.asList(allowedValues), typeOnPath));
        subQuery.select(subQueryRoot).where(predicate);

        return criteriaBuilder.in(root).value(subQuery);
//        return path.in(converter.convert(Arrays.asList(allowedValues), typeOnPath)).not();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(allowedValues);
        result = prime * result + ((converter == null) ? 0 : converter.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExclusivelyIn other = (ExclusivelyIn) obj;
        if (!Arrays.equals(allowedValues, other.allowedValues))
            return false;
        if (converter == null) {
            if (other.converter != null)
                return false;
        } else if (!converter.equals(other.converter))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "In [allowedValues=" + Arrays.toString(allowedValues) + ", converter=" + converter + "]";
    }
}
