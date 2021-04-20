package com.epam.esm.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tag")
public final class Tag {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_seq")
    @SequenceGenerator(name = "tag_id_seq", sequenceName = "tag_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "certificate_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id"))
    private Set<Certificate> certificates;

    protected Tag() {
    }

    public Tag(Long id,
               String name,
               Set<Certificate> certificates) {
        this.id = id;
        this.name = name;
        this.certificates = certificates;
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

    public Set<Certificate> getCertificates() {
        return certificates == null
                ? null
                : Collections.unmodifiableSet(certificates);
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
        private Set<Certificate> certificates;

        public Builder() {
        }

        private Builder(Tag tag) {
            id = tag.id;
            name = tag.name;
            certificates = tag.certificates;
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

        public Builder setCertificates(Set<Certificate> certificates) {
            this.certificates = certificates;
            return this;
        }

        public Tag build() {
            return new Tag(
                    id,
                    name,
                    certificates
            );
        }

    }


    private static final class Field {

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String CERTIFICATES = "certificates";

    }

}
