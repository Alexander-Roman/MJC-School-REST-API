package com.epam.esm.web.mapper;

import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.web.converter.StringToSortConverter;
import com.epam.esm.web.model.SortRequestDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = StringToSortConverter.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SortRequestMapper {

    SortRequest map(SortRequestDto sortRequestDto);

}
