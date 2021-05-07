package com.epam.esm.web.exception.model;

import java.util.Objects;

public final class FieldConstraintError extends ConstraintError implements NestedError {

    private static final long serialVersionUID = 1L;

    private final String field;
    private final Object value;

    public FieldConstraintError(String message, String code, String field, Object value) {
        super(message, code);
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
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
        FieldConstraintError that = (FieldConstraintError) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (field != null ? field.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "message='" + this.getMessage() + '\'' +
                ", code='" + this.getCode() + '\'' +
                ", field='" + field + '\'' +
                ", value=" + value +
                '}';
    }

}
