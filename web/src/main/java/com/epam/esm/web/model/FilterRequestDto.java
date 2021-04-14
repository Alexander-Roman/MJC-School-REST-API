package com.epam.esm.web.model;

import java.io.Serializable;
import java.util.Objects;

public class FilterRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String search;
    private final String tag;

    public FilterRequestDto(String search, String tag) {
        this.search = search;
        this.tag = tag;
    }

    public static FilterRequestDto.Builder builder() {
        return new FilterRequestDto.Builder();
    }

    public String getSearch() {
        return search;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterRequestDto that = (FilterRequestDto) o;
        return Objects.equals(search, that.search) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        int result = search != null ? search.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "search='" + search + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }


    public static final class Builder {

        private String search;
        private String tag;

        public Builder() {
        }

        private Builder(FilterRequestDto filterRequestDto) {
            search = filterRequestDto.search;
            tag = filterRequestDto.tag;
        }

        public static Builder from(FilterRequestDto filterRequestDto) {
            return new Builder(filterRequestDto);
        }

        public Builder setSearch(String search) {
            this.search = search;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public FilterRequestDto build() {
            return new FilterRequestDto(search, tag);
        }

    }

}
