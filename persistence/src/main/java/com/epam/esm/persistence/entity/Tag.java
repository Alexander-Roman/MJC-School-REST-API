package com.epam.esm.persistence.entity;

import java.util.Objects;

public final class Tag {

    private final Long id;
    private final String name;

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Tag.Builder builder() {
        return new Tag.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) &&
                Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    public static final class Builder {
        private Long id;
        private String name;

        public Builder() {
        }

        private Builder(Tag tag) {
            id = tag.id;
            name = tag.name;
        }

        public static Builder from(Tag tag) {
            return new Builder(tag);
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Tag build() {
            return new Tag(
                    id,
                    name
            );
        }

    }


    private static final class Field {

        public static final String ID = "id";
        public static final String NAME = "name";

    }

}
