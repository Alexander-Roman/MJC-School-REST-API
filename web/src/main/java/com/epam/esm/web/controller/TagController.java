package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.Tag_;
import com.epam.esm.persistence.specification.FindAllSpecification;
import com.epam.esm.service.TagService;
import com.epam.esm.web.assember.TagDtoAssembler;
import com.epam.esm.web.mapper.TagMapper;
import com.epam.esm.web.model.TagDto;
import com.epam.esm.web.validator.TagDtoValidator;
import com.epam.esm.web.validator.constraint.AllowedOrderProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/tags")
@Validated
public class TagController {

    private static final String MSG_CODE_ID_INVALID = "id.invalid";
    private static final String MSG_SORT_INVALID = "sort.invalid";
    private static final long MIN_ID = 1L;
    private static final String REL_ALL_TAGS = "tags";

    private final TagService tagService;
    private final TagDtoValidator tagDtoValidator;
    private final TagMapper tagMapper;
    private final PagedResourcesAssembler<Tag> tagPagedResourcesAssembler;
    private final TagDtoAssembler tagDtoAssembler;


    @Autowired
    public TagController(TagService tagService,
                         TagDtoValidator tagDtoValidator,
                         TagMapper tagMapper,
                         PagedResourcesAssembler<Tag> tagPagedResourcesAssembler,
                         TagDtoAssembler tagDtoAssembler) {
        this.tagService = tagService;
        this.tagDtoValidator = tagDtoValidator;
        this.tagMapper = tagMapper;
        this.tagPagedResourcesAssembler = tagPagedResourcesAssembler;
        this.tagDtoAssembler = tagDtoAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Tag tag = tagService.findById(id);
        TagDto tagDto = tagMapper.map(tag);

        tagDto.add(linkTo(methodOn(TagController.class).findById(id)).withSelfRel());
        tagDto.add(linkTo(methodOn(TagController.class).findAll(null)).withRel(REL_ALL_TAGS));
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<TagDto>> findAll(
            @AllowedOrderProperties(value = Tag_.NAME, message = MSG_SORT_INVALID) Pageable pageable) {
        Specification<Tag> specification = new FindAllSpecification<>();
        Page<Tag> tagPage = tagService.find(pageable, specification);
        PagedModel<TagDto> tagDtoPagedModel = tagPagedResourcesAssembler.toModel(tagPage, tagDtoAssembler);
        return new ResponseEntity<>(tagDtoPagedModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody TagDto tagDto, BindingResult bindingResult) throws BindException {
        tagDtoValidator.validate(tagDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Tag tag = tagMapper.map(tagDto);
        Tag created = tagService.create(tag);
        TagDto createdDto = tagMapper.map(created);

        createdDto.add(linkTo(methodOn(TagController.class).findById(createdDto.getId())).withSelfRel());
        createdDto.add(linkTo(methodOn(TagController.class).findAll(null)).withRel(REL_ALL_TAGS));
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TagDto> deleteById(
            @PathVariable("id")
            @Min(value = MIN_ID, message = MSG_CODE_ID_INVALID) Long id) {
        Tag deleted = tagService.deleteById(id);
        TagDto deletedDto = tagMapper.map(deleted);

        deletedDto.add(linkTo(methodOn(TagController.class).findAll(null)).withRel(REL_ALL_TAGS));
        return new ResponseEntity<>(deletedDto, HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity<TagDto> findMostPurchasedByTopAccount() {
        Tag tag = tagService.findMostPurchasedByTopAccount();
        TagDto tagDto = tagDtoAssembler.toModel(tag);

        tagDto.add(linkTo(methodOn(TagController.class).findAll(null)).withRel(REL_ALL_TAGS));
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

}
