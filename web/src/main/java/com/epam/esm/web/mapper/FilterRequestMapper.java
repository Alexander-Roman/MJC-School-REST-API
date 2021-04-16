package com.epam.esm.web.mapper;

import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.web.model.FilterRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FilterRequestMapper {

    @Mapping(source = "tag", target = "tagName")
    FilterRequest map(FilterRequestDto filterRequestDto);

}
