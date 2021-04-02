package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagValidator implements Validator<Tag> {

    private static final long MIN_ID = 1;
    private static final int MAX_TAG_NAME_LENGTH = 50;

    @Override
    public boolean isValid(Tag tag) {
        if (tag == null) {
            return false;
        }

        Long id = tag.getId();
        if (id != null && id < MIN_ID) {
            return false;
        }

        String name = tag.getName();
        return name != null
                && name.length() <= MAX_TAG_NAME_LENGTH
                && !name.trim().isEmpty();
    }

}
