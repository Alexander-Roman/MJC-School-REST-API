package com.epam.esm.security.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class EmailAndPasswordAuthenticationRequest {

    private final String email;
    private final String password;

    @JsonCreator
    public EmailAndPasswordAuthenticationRequest(@JsonProperty("email") String email,
                                                 @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAndPasswordAuthenticationRequest that = (EmailAndPasswordAuthenticationRequest) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
