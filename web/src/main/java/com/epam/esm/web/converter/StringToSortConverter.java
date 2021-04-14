package com.epam.esm.web.converter;

import com.epam.esm.persistence.model.Sort;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class StringToSortConverter implements Converter<String, Sort> {

    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    private static final String SPLITERATOR = ",";
    private static final int LENGTH_SORT_FIELD_ONLY = 1;
    private static final int INDEX_SORT_FIELD = 0;
    private static final int INDEX_SORT_DIRECTION = 1;

    @Override
    public Sort convert(@Nullable String value) {
        if (value == null) {
            return null;
        }
        String[] parameters = value.split(SPLITERATOR);
        String field = parameters[INDEX_SORT_FIELD];
        if (parameters.length == LENGTH_SORT_FIELD_ONLY) {
            return new Sort(field, DEFAULT_SORT_DIRECTION);
        }
        String directionValue = parameters[INDEX_SORT_DIRECTION];
        String upperCaseDirectionValue = directionValue.toUpperCase();
        Sort.Direction direction = Sort.Direction.valueOf(upperCaseDirectionValue);
        return new Sort(field, direction);
    }

}
