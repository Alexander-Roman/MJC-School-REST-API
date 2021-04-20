package com.epam.esm.service;


import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    private static final Long ID_VALID = 1L;
    private static final Long ID_INVALID = -1L;

    private static final Tag TAG_WITH_ID = new Tag(1L, "tagName", null);
    private static final Tag TAG_WITHOUT_ID = new Tag(null, "tagName", null);

    private static final Set<Tag> TAG_SET_WITH_ID = new HashSet<>(Collections.singletonList(TAG_WITH_ID));
    private static final Set<Tag> TAG_SET_WITHOUT_ID = new HashSet<>(Collections.singletonList(TAG_WITHOUT_ID));

    @Mock
    private TagRepository tagRepository;
    @Mock
    private Validator<Tag> tagValidator;
    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(tagRepository.findById(anyLong())).thenReturn(Optional.of(TAG_WITH_ID));
        lenient().when(tagRepository.findAll()).thenReturn(Collections.singletonList(TAG_WITH_ID));
        lenient().when(tagValidator.isValid(any())).thenReturn(true);
        lenient().when(tagRepository.save(any())).thenReturn(TAG_WITH_ID);
        lenient().when(tagRepository.findByName(anyString())).thenReturn(Optional.of(TAG_WITH_ID));
    }

    @Test
    public void findById_ShouldFindTagById() {
        //given
        //when
        Tag actual = tagService.findById(ID_VALID);
        //then
        Assertions.assertEquals(TAG_WITH_ID, actual);
    }

    @Test
    public void findById_WhenIdInvalid_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                tagService.findById(ID_INVALID)
        );
    }

    @Test
    public void findById_WhenIdIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                tagService.findById(null)
        );
    }

    @Test
    public void findById_WhenFoundNothing_ShouldThrowException() {
        //given
        lenient().when(tagRepository.findById(ID_VALID)).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                tagService.findById(ID_VALID)
        );
    }

    @Test
    public void findAll_ShouldFindListOfAllTags() {
        //given
        //when
        List<Tag> actual = tagService.findAll();
        //then
        List<Tag> expected = Collections.singletonList(TAG_WITH_ID);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void create_WhenTagInvalid_ShouldThrowException() {
        //given
        lenient().when(tagValidator.isValid(any())).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                tagService.create(TAG_WITHOUT_ID)
        );
    }

    @Test
    public void create_ShouldCreateTag() {
        //given
        lenient().when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        //when
        tagService.create(TAG_WITHOUT_ID);
        //then
        verify(tagRepository).save(TAG_WITHOUT_ID);
    }

    @Test
    public void create_ShouldReturnCreated() {
        //given
        //when
        Tag actual = tagService.create(TAG_WITHOUT_ID);
        //then
        Assertions.assertEquals(TAG_WITH_ID, actual);
    }

    @Test
    public void deleteById_WhenIdInvalid_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                tagService.deleteById(ID_INVALID)
        );
    }

    @Test
    public void deleteById_WhenIdIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                tagService.deleteById(null)
        );
    }

    @Test
    public void deleteById_WhenTagDoesNotExists_ShouldThrowException() {
        //given
        lenient().when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                tagService.deleteById(ID_VALID)
        );
    }

    @Test
    public void deleteById_ShouldDeleteTag() {
        //given
        //when
        tagService.deleteById(ID_VALID);
        //then
        verify(tagRepository).delete(1L);
    }

    @Test
    public void createIfNotExist_WhenTagSetIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                tagService.createIfNotExist(null)
        );
    }

    @Test
    public void createIfNotExist_WhenOneOfTagsInvalid_ShouldThrowException() {
        //given
        Tag tagInvalid = new Tag(null, null, null);
        lenient().when(tagValidator.isValid(TAG_WITH_ID)).thenReturn(true);
        lenient().when(tagValidator.isValid(tagInvalid)).thenReturn(false);
        Set<Tag> tags = new HashSet<>(Arrays.asList(TAG_WITH_ID, tagInvalid));
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                tagService.createIfNotExist(tags)
        );
    }

    @Test
    public void createIfNotExist_WhenTagSetIsEmpty_ShouldDoNothing() {
        //given
        //when
        tagService.createIfNotExist(Collections.emptySet());
        //then
        verify(tagRepository, never()).findByName(anyString());
        verify(tagRepository, never()).save(any());
    }

    @Test
    public void createIfNotExist_WhenTagSetIsEmpty_ShouldReturnEmptySet() {
        //given
        //when
        Set<Tag> actual = tagService.createIfNotExist(Collections.emptySet());
        //then
        Set<Tag> expected = Collections.emptySet();
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void createIfNotExist_WhenExists_ShouldReturnTag() {
        //given
        //when
        Set<Tag> actual = tagService.createIfNotExist(TAG_SET_WITHOUT_ID);
        //then
        Assertions.assertEquals(TAG_SET_WITH_ID, actual);
    }

    @Test
    public void createIfNotExist_WhenExists_ShouldNotCreateTag() {
        //given
        //when
        tagService.createIfNotExist(TAG_SET_WITHOUT_ID);
        //then
        verify(tagRepository, never()).save(any());
    }

    @Test
    public void createIfNotExist_WhenNotFound_ShouldCreateTag() {
        //given
        lenient().when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        //when
        tagService.createIfNotExist(TAG_SET_WITHOUT_ID);
        //then
        verify(tagRepository).save(TAG_WITHOUT_ID);
    }

    @Test
    public void createIfNotExist_WhenNotFound_ShouldReturnCreated() {
        //given
        lenient().when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        //when
        Set<Tag> actual = tagService.createIfNotExist(TAG_SET_WITHOUT_ID);
        //then
        Assertions.assertEquals(TAG_SET_WITH_ID, actual);
    }

}
