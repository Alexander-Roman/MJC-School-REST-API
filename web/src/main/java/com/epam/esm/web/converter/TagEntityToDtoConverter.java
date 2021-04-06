package com.epam.esm.web.converter;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.model.TagDto;
import org.modelmapper.AbstractConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TagEntityToDtoConverter extends AbstractConverter<Tag, TagDto> implements Converter<Tag, TagDto> {

    @Override
    public TagDto convert(Tag tag) {
        return new TagDto(
                tag.getId(),
                tag.getName()
        );
    }

}
