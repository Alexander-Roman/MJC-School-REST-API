package com.epam.esm.persistence.entity;

import java.util.Objects;

public final class Tag {

    private final Long tagId;
    private final String tagName;

    public Tag(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public Long getTagId() {
        return tagId;
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
        Tag tag = (Tag) o;
        return Objects.equals(tagId, tag.tagId) &&
                Objects.equals(tagName, tag.tagName);
    }

    @Override
    public int hashCode() {
        int result = tagId != null ? tagId.hashCode() : 0;
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                '}';
    }


    public static final class Builder {
        private Long tagId;
        private String tagName;

        public Builder() {
        }

        private Builder(Tag tag) {
            tagId = tag.tagId;
            tagName = tag.tagName;
        }

        private static Builder from(Tag tag) {
            return new Builder(tag);
        }

        public Builder setTagName(String tagName) {
            this.tagName = tagName;
            return this;
        }

        public Tag build() {
            return new Tag(
                    tagId,
                    tagName
            );
        }

    }


    private static final class Field {

        public static final String ID = "tagId";
        public static final String NAME = "tagName";

    }

}
