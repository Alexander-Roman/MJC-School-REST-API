package com.epam.esm.web.model;

import com.epam.esm.web.validator.constraint.NullOrNotBlank;
import com.epam.esm.web.validator.group.CertificateCreate;
import com.epam.esm.web.validator.group.CertificateUpdate;
import com.epam.esm.web.validator.group.PurchaseCreate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;


public final class CertificateDto extends RepresentationModel<CertificateDto> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1L, message = "{certificate.dto.id.min}")
    @NotNull(message = "{certificate.dto.id.null}", groups = {CertificateUpdate.class, PurchaseCreate.class})
    private final Long id;

    @NotNull(message = "{certificate.dto.create.name.null}", groups = CertificateCreate.class)
    @NullOrNotBlank(message = "{certificate.dto.name.blank}")
    @Size(max = 150, message = "{certificate.dto.name.size}")
    private final String name;

    @NotNull(message = "{certificate.dto.create.description.null}", groups = CertificateCreate.class)
    @NullOrNotBlank(message = "{certificate.dto.description.blank}")
    @Size(max = 255, message = "{certificate.dto.description.size}")
    private final String description;

    @NotNull(message = "{certificate.dto.create.price.null}", groups = CertificateCreate.class)
    @DecimalMin(value = "0.0", message = "{certificate.dto.price.negative}")
    @DecimalMax(value = "99999.99", message = "{certificate.dto.price.overmuch}")
    private final BigDecimal price;

    @NotNull(message = "{certificate.dto.create.duration.null}", groups = CertificateCreate.class)
    @Min(value = 1, message = "{certificate.dto.duration.min}")
    private final Integer duration;


    private final LocalDateTime createDate;
    private final LocalDateTime lastUpdateDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(value = 1, message = "{certificate.dto.quantity.min}", groups = PurchaseCreate.class)
    private final Integer quantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "{certificate.dto.create.tags.null}", groups = CertificateCreate.class)
    private final Set<@Valid @NotNull(message = "{tag.dto.null}") TagDto> tags;

    @JsonCreator
    public CertificateDto(@JsonProperty("id") Long id,
                          @JsonProperty("name") String name,
                          @JsonProperty("description") String description,
                          @JsonProperty("price") BigDecimal price,
                          @JsonProperty("duration") Integer duration,
                          @JsonProperty("createDate") LocalDateTime createDate,
                          @JsonProperty("lastUpdateDate") LocalDateTime lastUpdateDate,
                          @JsonProperty("quantity") Integer quantity,
                          @JsonProperty("tags") Set<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.quantity = quantity;
        this.tags = tags;
    }

    public static CertificateDto.Builder builder() {
        return new CertificateDto.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public Integer getQuantity() {
        return quantity;
    }

    public Set<TagDto> getTags() {
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
        CertificateDto that = (CertificateDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", quantity=" + quantity +
                ", tags=" + tags +
                '}';
    }


    public static final class Builder {

        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer duration;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;
        private Integer quantity;
        private Set<TagDto> tags;

        public Builder() {
        }

        private Builder(CertificateDto certificateDto) {
            id = certificateDto.id;
            name = certificateDto.name;
            description = certificateDto.description;
            price = certificateDto.price;
            duration = certificateDto.duration;
            createDate = certificateDto.createDate;
            lastUpdateDate = certificateDto.lastUpdateDate;
            tags = certificateDto.tags;
            quantity = certificateDto.quantity;
        }

        public static Builder from(CertificateDto certificateDto) {
            return new Builder(certificateDto);
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
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

        public Builder setQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setTags(Set<TagDto> tags) {
            this.tags = tags;
            return this;
        }

        public CertificateDto build() {
            return new CertificateDto(
                    id,
                    name,
                    description,
                    price,
                    duration,
                    createDate,
                    lastUpdateDate,
                    quantity,
                    tags
            );
        }

    }

}
