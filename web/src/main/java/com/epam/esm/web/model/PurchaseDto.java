package com.epam.esm.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public final class PurchaseDto extends RepresentationModel<PurchaseDto> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;

    @NotNull(message = "{purchase.dto.account.null}")
    @Valid
    private final AccountDto account;

    private final BigDecimal cost;

    private final LocalDateTime date;

    @NotNull(message = "{purchase.dto.items.null}")
    @NotEmpty(message = "{purchase.dto.items.empty}")
    private final List<@Valid @NotNull(message = "{certificate.dto.null}") CertificateDto> items;

    @JsonCreator
    public PurchaseDto(@JsonProperty("id") Long id,
                       @JsonProperty("account") AccountDto account,
                       @JsonProperty("cost") BigDecimal cost,
                       @JsonProperty("date") LocalDateTime date,
                       @JsonProperty("items") List<CertificateDto> items) {
        this.id = id;
        this.account = account;
        this.cost = cost;
        this.date = date;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public AccountDto getAccount() {
        return account;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<CertificateDto> getItems() {
        return items == null
                ? null
                : Collections.unmodifiableList(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchaseDto that = (PurchaseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(account, that.account) &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(date, that.date) &&
                Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", account=" + account +
                ", cost=" + cost +
                ", date=" + date +
                ", items=" + items +
                '}';
    }

}
