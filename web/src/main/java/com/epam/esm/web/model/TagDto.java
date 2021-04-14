package com.epam.esm.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public final class TagDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String name;

    @JsonCreator
    public TagDto(@JsonProperty("id") Long id,
                  @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
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
        TagDto tagDto = (TagDto) o;
        return Objects.equals(id, tagDto.id) &&
                Objects.equals(name, tagDto.name);
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

        private Builder(TagDto tagDto) {
            id = tagDto.id;
            name = tagDto.name;
        }

        public static Builder from(TagDto tagDto) {
            return new Builder(tagDto);
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public TagDto build() {
            return new TagDto(id, name);
        }

    }


    public static final class Field {

        public static final String ID = "id";
        public static final String NAME = "name";

    }

}
