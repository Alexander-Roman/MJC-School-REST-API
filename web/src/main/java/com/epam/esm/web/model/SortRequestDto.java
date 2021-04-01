package com.epam.esm.web.model;

import java.io.Serializable;
import java.util.Objects;

public class SortRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String sort;
    private final String thenSort;

    public SortRequestDto(String sort, String thenSort) {
        this.sort = sort;
        this.thenSort = thenSort;
    }

    public String getSort() {
        return sort;
    }

    public String getThenSort() {
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
        SortRequestDto that = (SortRequestDto) o;
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
                "sort='" + sort + '\'' +
                ", thenSort='" + thenSort + '\'' +
                '}';
    }

}
