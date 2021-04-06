package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.logic.TagService;
import com.epam.esm.web.model.TagDto;
import com.epam.esm.web.validator.TagDtoValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;
    private final TagDtoValidator tagDtoValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public TagController(TagService tagService,
                         TagDtoValidator tagDtoValidator,
                         ModelMapper modelMapper) {
        this.tagService = tagService;
        this.tagDtoValidator = tagDtoValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(@PathVariable("id") Long id) {
        Tag tag = tagService.findById(id);
        TagDto tagDto = modelMapper.map(tag, TagDto.class);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        List<Tag> tagList = tagService.findAll();
        List<TagDto> tagDtoList = tagList
                .stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(tagDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody TagDto tagDto, BindingResult bindingResult) throws BindException {
        tagDtoValidator.validate(tagDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Tag tag = modelMapper.map(tagDto, Tag.class);
        Tag created = tagService.create(tag);
        TagDto createdDto = modelMapper.map(created, TagDto.class);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TagDto> deleteById(@PathVariable("id") Long id) {
        Tag deleted = tagService.deleteById(id);
        TagDto deletedDto = modelMapper.map(deleted, TagDto.class);
        return new ResponseEntity<>(deletedDto, HttpStatus.OK);
    }

}
