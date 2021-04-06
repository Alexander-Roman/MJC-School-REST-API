package com.epam.esm.web.converter;

import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.web.model.FilterRequestDto;
import org.modelmapper.AbstractConverter;
import org.springframework.core.convert.converter.Converter;

public class FilterRequestDtoToModelConverter extends AbstractConverter<FilterRequestDto, FilterRequest> implements Converter<FilterRequestDto, FilterRequest> {

    @Override
    public FilterRequest convert(FilterRequestDto filterRequestDto) {
        String search = filterRequestDto.getSearch();
        String tagName = filterRequestDto.getTag();
        return new FilterRequest(search, tagName);
    }

}
