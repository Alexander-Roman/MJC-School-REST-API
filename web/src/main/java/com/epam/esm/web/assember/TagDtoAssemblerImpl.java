package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.mapper.TagMapper;
import com.epam.esm.web.model.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagDtoAssemblerImpl extends RepresentationModelAssemblerSupport<Tag, TagDto> implements TagDtoAssembler {

    private final TagMapper tagMapper;

    @Autowired
    public TagDtoAssemblerImpl(TagMapper tagMapper) {
        super(TagController.class, TagDto.class);
        this.tagMapper = tagMapper;
    }

    @Override
    public TagDto toModel(Tag entity) {
        TagDto tagDto = tagMapper.map(entity);
        Long id = tagDto.getId();
        return tagDto.add(linkTo(methodOn(TagController.class).findById(id)).withSelfRel());
    }

}
