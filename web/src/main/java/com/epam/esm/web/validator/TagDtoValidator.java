package com.epam.esm.web.validator;

import com.epam.esm.web.model.TagDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TagDtoValidator implements Validator {

    private static final long MIN_ID = 1;
    private static final int MAX_TAG_NAME_LENGTH = 50;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return TagDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        TagDto tagDto = (TagDto) object;

        Long id = tagDto.getId();
        if (id != null && id < MIN_ID) {
            errors.rejectValue(TagDto.Field.ID, "tag.dto.id.invalid", "Id should be not less than 1!");
        }

        String name = tagDto.getName();

        if (name == null) {
            errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.null", "Tag name required!");
        } else {
            if (name.length() > MAX_TAG_NAME_LENGTH) {
                errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.invalid", "Tag name should be not greater than 50 characters!");
            }
            String trimmed = name.trim();
            if ("".equals(trimmed)) {
                errors.rejectValue(TagDto.Field.NAME, "tag.dto.name.empty", "Blank tag name is not allowed!");
            }
        }
    }

}
