package com.epam.esm.web.exception.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class BindApiError extends ApiError {

    private static final long serialVersionUID = 1L;

    private final List<String> errors;

    public BindApiError(String message, String code, List<String> errors) {
        super(message, code);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors == null
                ? null
                : Collections.unmodifiableList(errors);
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
        BindApiError bindError = (BindApiError) o;
        return Objects.equals(errors, bindError.errors);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "message='" + getMessage() + '\'' +
                ", code='" + getCode() + '\'' +
                ", errors=" + errors +
                '}';
    }

}
