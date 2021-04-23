package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TagValidator implements Validator<Tag> {

    private static final long MIN_ID = 1;
    private static final int MAX_TAG_NAME_LENGTH = 50;
    private static final String REGEX_NAME = "[a-zа-я0-9]+";
    private static final Pattern PATTERN_NAME = Pattern.compile(REGEX_NAME);

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
        return this.isValid(name);
    }

    private boolean isValid(String name) {
        if (name == null) {
            return false;
        }
        if (name.length() > MAX_TAG_NAME_LENGTH) {
            return false;
        }
        Matcher matcher = PATTERN_NAME.matcher(name);
        return matcher.matches();
    }

}
