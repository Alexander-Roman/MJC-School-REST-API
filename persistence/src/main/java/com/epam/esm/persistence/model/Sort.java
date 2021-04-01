package com.epam.esm.persistence.model;

import java.util.Objects;

public final class Sort {

    private final String field;
    private final Direction direction;

    public Sort(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public static Sort asc(String field) {
        return new Sort(field, Direction.ASC);
    }

    public static Sort desc(String field) {
        return new Sort(field, Direction.DESC);
    }

    public String getField() {
        return field;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sort sort = (Sort) o;
        return Objects.equals(field, sort.field) &&
                direction == sort.direction;
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "field='" + field + '\'' +
                ", direction=" + direction +
                '}';
    }


    public enum Direction {
        ASC("ASC"),
        DESC("DESC");

        private final String keyword;

        Direction(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword() {
            return keyword;
        }

    }

}
