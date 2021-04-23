package com.epam.esm.web.validator;

import com.epam.esm.web.model.TagDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TagDtoValidatorImpl implements TagDtoValidator {

    private static final int MAX_TAG_NAME_LENGTH = 50;
    private static final String REGEX_NAME = "[a-zA-Zа-яА-Я0-9]+";
    private static final Pattern PATTERN_NAME = Pattern.compile(REGEX_NAME);

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        TagDto tagDto = (TagDto) object;

        String name = tagDto.getName();
        if (name == null) {
            errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.null", "Tag name required!");
        } else {
            if (name.length() > MAX_TAG_NAME_LENGTH) {
                errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.length", "Tag name should be not greater than 50 characters!");
            }
            if (name.trim().isEmpty()) {
                errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.blank", "Blank tag name is not allowed!");
            }
            Matcher matcher = PATTERN_NAME.matcher(name);
            if (!matcher.matches()) {
                errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.invalid", "Tag name can only contain letters and digits!");
            }
        }
    }

}
