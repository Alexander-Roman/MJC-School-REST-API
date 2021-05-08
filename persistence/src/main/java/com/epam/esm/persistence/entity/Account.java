package com.epam.esm.persistence.entity;

import com.epam.esm.persistence.audit.listener.AuditListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "account")
@EntityListeners(AuditListener.class)
public class Account implements Identifiable {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "blocked")
    private Boolean blocked = false;

    protected Account() {
    }

    public Account(Long id,
                   String email,
                   String password,
                   String name,
                   Role role,
                   Boolean blocked) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.blocked = blocked;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(email, account.email) &&
                Objects.equals(password, account.password) &&
                Objects.equals(name, account.name) &&
                role == account.role &&
                Objects.equals(blocked, account.blocked);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (blocked != null ? blocked.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", blocked=" + blocked +
                '}';
    }


    public static final class Builder {

        private Long id;
        private String email;
        private String password;
        private String name;
        private Role role = Role.USER;
        private Boolean blocked = false;

        public Builder() {
        }

        private Builder(Account account) {
            id = account.id;
            email = account.email;
            password = account.password;
            name = account.name;
            role = account.role;
            blocked = account.blocked;
        }

        public static Builder from(Account account) {
            return new Builder(account);
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setBlocked(Boolean blocked) {
            this.blocked = blocked;
            return this;
        }

        public Account build() {
            return new Account(id, email, password, name, role, blocked);
        }

    }

}
