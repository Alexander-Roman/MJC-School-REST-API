package com.epam.esm.web.exception.model;

import java.io.Serializable;
import java.util.Objects;

public final class FieldApiError implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String field;
    private final Object value;
    private final String message;
    private final String code;

    public FieldApiError(String field,
                         Object value,
                         String message,
                         String code) {
        this.field = field;
        this.value = value;
        this.message = message;
        this.code = code;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
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
        FieldApiError that = (FieldApiError) object;
        return Objects.equals(field, that.field) &&
                Objects.equals(value, that.value) &&
                Objects.equals(message, that.message) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "field='" + field + '\'' +
                ", value=" + value +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

}
