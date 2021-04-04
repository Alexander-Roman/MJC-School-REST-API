package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.logic.TagService;
import com.epam.esm.web.model.TagDto;
import com.epam.esm.web.validator.TagDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;
    private final TagDtoValidator tagDtoValidator;
    private final Converter<TagDto, Tag> tagDtoToEntityConverter;

    @Autowired
    public TagController(TagService tagService,
                         TagDtoValidator tagDtoValidator,
                         Converter<TagDto, Tag> tagDtoToEntityConverter) {
        this.tagService = tagService;
        this.tagDtoValidator = tagDtoValidator;
        this.tagDtoToEntityConverter = tagDtoToEntityConverter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(@PathVariable("id") Long id) {
        Optional<Tag> found = tagService.findById(id);
        if (!found.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exists! ID: " + id);
        }
        Tag tag = found.get();
        TagDto tagDto = TagDto.fromEntity(tag);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        List<Tag> tagList = tagService.findAll();
        List<TagDto> tagDtoList = tagList
                .stream()
                .map(TagDto::fromEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tagDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody TagDto tagDto, BindingResult bindingResult) throws BindException {
        tagDtoValidator.validate(tagDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Tag tag = tagDtoToEntityConverter.convert(tagDto);
        Tag created = tagService.create(tag);
        TagDto createdDto = TagDto.fromEntity(created);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TagDto> deleteById(@PathVariable("id") Long id) {
        Optional<Tag> found = tagService.findById(id);
        if (!found.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag does not exists! ID: " + id);
        }
        tagService.deleteById(id);
        Tag tag = found.get();
        TagDto tagDto = TagDto.fromEntity(tag);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

}
