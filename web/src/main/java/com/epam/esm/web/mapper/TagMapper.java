package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.model.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "name", expression = "java( tagDto.getName().toLowerCase() )")
    Tag map(TagDto tagDto);

    TagDto map(Tag tag);

}
