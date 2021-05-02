package com.epam.esm.web.model;

import com.epam.esm.web.validator.group.PurchaseCreate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


public final class AccountDto extends RepresentationModel<AccountDto> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1L, message = "{account.dto.id.min}")
    @NotNull(message = "{account.dto.id.null}", groups = PurchaseCreate.class)
    private final Long id;

    private final String name;

    @JsonCreator
    public AccountDto(@JsonProperty("id") Long id,
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
        AccountDto that = (AccountDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
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

}
