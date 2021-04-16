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

}
