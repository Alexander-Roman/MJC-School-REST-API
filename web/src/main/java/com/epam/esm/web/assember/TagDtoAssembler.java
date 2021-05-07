package com.epam.esm.web.assember;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.model.TagDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * Interface for components that convert Tag type into TagDto as RepresentationModel
 */
public interface TagDtoAssembler extends RepresentationModelAssembler<Tag, TagDto> {

}
