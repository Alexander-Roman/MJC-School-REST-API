package com.epam.esm.web.mapper;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.model.TagDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

    Tag map(TagDto tagDto);

    TagDto map(Tag tag);

}
