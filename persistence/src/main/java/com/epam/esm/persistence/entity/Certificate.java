package com.epam.esm.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public final class Certificate {

    private final Long certificateId;
    private final String certificateName;
    private final String description;
    private final BigDecimal price;
    private final Integer duration;
    private final LocalDateTime createDate;
    private final LocalDateTime lastUpdateDate;
    private final Set<Tag> tags;

    public Certificate(Long certificateId,
                       String certificateName,
                       String description,
                       BigDecimal price,
                       Integer duration,
                       LocalDateTime createDate,
                       LocalDateTime lastUpdateDate,
                       Set<Tag> tags) {
        this.certificateId = certificateId;
        this.certificateName = certificateName;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public Long getCertificateId() {
        return certificateId;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getDuration() {
        return duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public Set<Tag> getTags() {
        return tags == null
                ? null
                : Collections.unmodifiableSet(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Certificate that = (Certificate) o;
        return Objects.equals(certificateId, that.certificateId) &&
                Objects.equals(certificateName, that.certificateName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        int result = certificateId != null ? certificateId.hashCode() : 0;
        result = 31 * result + (certificateName != null ? certificateName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "certificateId=" + certificateId +
                ", certificateName='" + certificateName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tags=" + tags +
                '}';
    }


    public static final class Builder {

        private Long certificateId;
        private String certificateName;
        private String description;
        private BigDecimal price;
        private Integer duration;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;
        private Set<Tag> tags;

        public Builder() {
        }

        private Builder(Certificate certificate) {
            certificateId = certificate.certificateId;
            certificateName = certificate.certificateName;
            description = certificate.description;
            price = certificate.price;
            duration = certificate.duration;
            createDate = certificate.createDate;
            lastUpdateDate = certificate.lastUpdateDate;
            tags = certificate.tags;
        }

        public static Builder from(Certificate certificate) {
            return new Builder(certificate);
        }

        public Builder setCertificateId(Long certificateId) {
            this.certificateId = certificateId;
            return this;
        }

        public Builder setCertificateName(String certificateName) {
            this.certificateName = certificateName;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public Builder setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setLastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Builder setTags(Set<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public Certificate build() {
            return new Certificate(
                    certificateId,
                    certificateName,
                    description,
                    price,
                    duration,
                    createDate,
                    lastUpdateDate,
                    tags);
        }

    }

    public static final class Field {

        public static final String ID = "certificateId";
        public static final String NAME = "certificateName";
        public static final String DESCRIPTION = "description";
        public static final String PRICE = "price";
        public static final String DURATION = "duration";
        public static final String CREATE_DATE = "createDate";
        public static final String LAST_UPDATE_DATE = "lastUpdateDate";
        public static final String TAGS = "tags";

    }

}
