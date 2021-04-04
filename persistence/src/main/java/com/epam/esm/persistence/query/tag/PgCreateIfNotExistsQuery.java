package com.epam.esm.persistence.query.tag;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.UpdateQuery;

import java.util.*;
import java.util.stream.Collectors;

public class PgCreateIfNotExistsQuery implements UpdateQuery<Tag> {

    private static final String TEMPLATE_SQL_SAVE_IF_NOT_EXIST = "\n" +
            "INSERT INTO tag (name) VALUES %s ON CONFLICT (name) DO NOTHING \n";
    private static final String TEMPLATE_VALUE_SET = "(%s)";
    private static final String VALUE_SET_DELIMITER = ", ";

    private final Collection<? extends Tag> tags;

    public PgCreateIfNotExistsQuery(Collection<? extends Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String getSql() {
        List<String> parameters = new ArrayList<>();
        int size = tags.size();
        for (int i = 1; i <= size; i++) {
            String parameter = ":tagName" + i;
            parameters.add(parameter);
        }
        String valueSet = parameters
                .stream()
                .map(tagName -> String.format(TEMPLATE_VALUE_SET, tagName))
                .collect(Collectors.joining(VALUE_SET_DELIMITER));
        return String.format(TEMPLATE_SQL_SAVE_IF_NOT_EXIST, valueSet);
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> namedParameters = new HashMap<>();
        int count = 0;
        for (Tag tag : tags) {
            ++count;
            String tagName = tag.getName();
            String key = "tagName" + count;
            namedParameters.put(key, tagName);
        }
        return Collections.unmodifiableMap(namedParameters);
    }

}
