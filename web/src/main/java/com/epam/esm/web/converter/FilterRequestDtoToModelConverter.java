package com.epam.esm.web.converter;

import com.epam.esm.service.model.FilterRequest;
import com.epam.esm.web.model.FilterRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FilterRequestDtoToModelConverter implements Converter<FilterRequestDto, FilterRequest> {

    @Override
    public FilterRequest convert(FilterRequestDto filterRequestDto) {
        String search = filterRequestDto.getSearch();
        String tagName = filterRequestDto.getTag();
        return new FilterRequest(search, tagName);
    }

}
