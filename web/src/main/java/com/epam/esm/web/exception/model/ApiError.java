package com.epam.esm.web.exception.model;

import java.io.Serializable;
import java.util.Objects;

public class ApiError implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final String code;

    public ApiError(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

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
        ApiError apiError = (ApiError) o;
        return Objects.equals(message, apiError.message) &&
                Objects.equals(code, apiError.code);
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
