package com.epam.esm.persistence.model;

import java.util.Objects;

public class SortRequest {

    private final Sort sort;
    private final Sort thenSort;

    public SortRequest(Sort sort, Sort thenSort) {
        this.sort = sort;
        this.thenSort = thenSort;
    }

    public Sort getSort() {
        return sort;
    }

    public Sort getThenSort() {
        return thenSort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SortRequest that = (SortRequest) o;
        return Objects.equals(sort, that.sort) &&
                Objects.equals(thenSort, that.thenSort);
    }

    @Override
    public int hashCode() {
        int result = sort != null ? sort.hashCode() : 0;
        result = 31 * result + (thenSort != null ? thenSort.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "sort=" + sort +
                ", thenSort=" + thenSort +
                '}';
    }

}
