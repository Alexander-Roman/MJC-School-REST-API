package com.epam.esm.web.converter;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.model.TagDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TagDtoToEntityConverter implements Converter<TagDto, Tag> {

    @Override
    public Tag convert(TagDto tagDto) {
        Long id = tagDto.getId();
        String name = tagDto.getName();
        return new Tag(id, name);
    }

}
