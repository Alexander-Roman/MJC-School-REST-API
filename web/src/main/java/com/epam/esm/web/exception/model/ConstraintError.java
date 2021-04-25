package com.epam.esm.web.exception.model;

import java.util.Objects;

public class ConstraintError implements NestedError {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final String code;

    public ConstraintError(String message, String code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstraintError that = (ConstraintError) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

}
