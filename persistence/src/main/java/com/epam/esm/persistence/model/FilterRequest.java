package com.epam.esm.persistence.model;

import java.util.Objects;

public class FilterRequest {

    private final String search;
    private final String tagName;

    public FilterRequest(String search, String tagName) {
        this.search = search;
        this.tagName = tagName;
    }

    public String getSearch() {
        return search;
    }

    public String getTagName() {
        return tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterRequest that = (FilterRequest) o;
        return Objects.equals(search, that.search) &&
                Objects.equals(tagName, that.tagName);
    }

    @Override
    public int hashCode() {
        int result = search != null ? search.hashCode() : 0;
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "search='" + search + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
    }

}
