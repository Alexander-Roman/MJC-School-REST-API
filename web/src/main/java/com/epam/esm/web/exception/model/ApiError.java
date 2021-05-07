package com.epam.esm.web.exception.model;

import java.util.Objects;

public class ApiError implements RootError {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final Integer code;

    public ApiError(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ApiError rootApiErrorImpl = (ApiError) object;
        return Objects.equals(message, rootApiErrorImpl.message) &&
                Objects.equals(code, rootApiErrorImpl.code);
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
                ", code=" + code +
                '}';
    }

}
