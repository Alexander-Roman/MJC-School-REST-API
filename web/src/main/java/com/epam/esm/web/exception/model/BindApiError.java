package com.epam.esm.web.exception.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class BindApiError extends ApiError {

    private static final long serialVersionUID = 1L;

    private final List<FieldApiError> errors;

    public BindApiError(String message,
                        Integer code,
                        List<FieldApiError> errors) {
        super(message, code);
        this.errors = errors;
    }

    public List<FieldApiError> getErrors() {
        return errors == null
                ? null
                : Collections.unmodifiableList(errors);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        BindApiError that = (BindApiError) object;
        return Objects.equals(errors, that.errors);
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
                "message='" + this.getMessage() + '\'' +
                ", code=" + this.getCode() +
                ", errors=" + errors +
                '}';
    }

}
