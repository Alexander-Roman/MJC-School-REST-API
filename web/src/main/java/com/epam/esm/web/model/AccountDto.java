package com.epam.esm.web.model;

import com.epam.esm.web.validator.constraint.AccountPasswordConfirm;
import com.epam.esm.web.validator.group.AccountCreate;
import com.epam.esm.web.validator.group.PurchaseCreate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@AccountPasswordConfirm(groups = AccountCreate.class)
public final class AccountDto extends RepresentationModel<AccountDto> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1L, message = "{account.dto.id.min}")
    @NotNull(message = "{account.dto.id.not.null}", groups = PurchaseCreate.class)
    @Null(message = "{account.dto.id.null}", groups = AccountCreate.class)
    private final Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "{account.dto.email.not.null}", groups = AccountCreate.class)
    @Email(message = "{account.dto.email.invalid}")
    @Size(max = 255, message = "{account.dto.email.size}")
    private final String email;

    @NotNull(message = "{account.dto.name.not.null}", groups = AccountCreate.class)
    @Size(min = 3, max = 255, message = "{account.dto.name.size}")
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+([\\s\\-][a-zA-Zа-яА-Я]+)*", message = "{account.dto.name.pattern}")
    private final String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{account.dto.password.not.blank}", groups = AccountCreate.class)
    private final String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{account.dto.password.confirm.not.blank}", groups = AccountCreate.class)
    private final String passwordConfirm;

    @JsonCreator
    public AccountDto(@JsonProperty("id") Long id,
                      @JsonProperty("email") String email,
                      @JsonProperty("name") String name,
                      @JsonProperty("password") String password,
                      @JsonProperty("passwordConfirm") String passwordConfirm) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AccountDto that = (AccountDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(name, that.name) &&
                Objects.equals(password, that.password) &&
                Objects.equals(passwordConfirm, that.passwordConfirm);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (passwordConfirm != null ? passwordConfirm.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                '}';
    }

}
