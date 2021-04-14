package com.epam.esm.web.controller;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.web.mapper.TagMapper;
import com.epam.esm.web.model.TagDto;
import com.epam.esm.web.validator.TagDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tag")
@Validated
public class TagController {

    private static final String MSG_ID_INVALID = "Invalid id parameter!";

    private final TagService tagService;
    private final TagDtoValidator tagDtoValidator;
    private final TagMapper tagMapper;

    @Autowired
    public TagController(TagService tagService,
                         TagDtoValidator tagDtoValidator,
                         TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagDtoValidator = tagDtoValidator;
        this.tagMapper = tagMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(
            @PathVariable("id")
            @Min(value = 1L, message = MSG_ID_INVALID) Long id) {
        Tag tag = tagService.findById(id);
        TagDto tagDto = tagMapper.map(tag);
        return new ResponseEntity<>(tagDto, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<TagDto>> findAll() {
        List<Tag> tagList = tagService.findAll();
        List<TagDto> tagDtoList = tagList
                .stream()
                .map(tagMapper::map)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tagDtoList, HttpStatus.OK);
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
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TagDto> deleteById(
            @PathVariable("id")
            @Min(value = 1L, message = MSG_ID_INVALID) Long id) {
        Tag deleted = tagService.deleteById(id);
        TagDto deletedDto = tagMapper.map(deleted);
        return new ResponseEntity<>(deletedDto, HttpStatus.OK);
    }

}
