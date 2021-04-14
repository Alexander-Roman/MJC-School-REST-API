package com.epam.esm.service;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
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

    private static final Tag TAG_WITH_ID = new Tag(1L, "tagName");
    private static final Tag TAG_WITHOUT_ID = new Tag(null, "tagName");

    private static final Set<Tag> TAG_SET_WITH_ID = new HashSet<>(Collections.singletonList(TAG_WITH_ID));
    private static final Set<Tag> TAG_SET_WITHOUT_ID = new HashSet<>(Collections.singletonList(TAG_WITHOUT_ID));

    @Mock
    private TagDao tagDao;
    @Mock
    private CertificateTagService certificateTagService;
    @Mock
    private Validator<Tag> tagValidator;
    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(tagDao.findById(anyLong())).thenReturn(Optional.of(TAG_WITH_ID));
        lenient().when(tagDao.findAll()).thenReturn(Collections.singletonList(TAG_WITH_ID));
        lenient().when(tagValidator.isValid(any())).thenReturn(true);
        lenient().when(tagDao.create(any())).thenReturn(TAG_WITH_ID);
        lenient().when(tagDao.findByName(anyString())).thenReturn(Optional.of(TAG_WITH_ID));
    }

    @Test
    public void testFindByIdShouldFindTagById() {
        //given
        //when
        Tag actual = tagService.findById(ID_VALID);
        //then
        Assertions.assertEquals(TAG_WITH_ID, actual);
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                tagService.findById(ID_INVALID)
        );
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                tagService.findById(null)
        );
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenFoundNothing() {
        //given
        lenient().when(tagDao.findById(ID_VALID)).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                tagService.findById(ID_VALID)
        );
    }

    @Test
    public void testFindAllShouldFindListOfAllTags() {
        //given
        //when
        List<Tag> actual = tagService.findAll();
        //then
        List<Tag> expected = Collections.singletonList(TAG_WITH_ID);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCreateShouldThrowExceptionWhenTagInvalid() {
        //given
        lenient().when(tagValidator.isValid(any())).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                tagService.create(TAG_WITHOUT_ID)
        );
    }

    @Test
    public void testCreateShouldCreateTag() {
        //given
        lenient().when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
        //when
        tagService.create(TAG_WITHOUT_ID);
        //then
        verify(tagDao).create(TAG_WITHOUT_ID);
    }

    @Test
    public void testCreateShouldReturnCreated() {
        //given
        //when
        Tag actual = tagService.create(TAG_WITHOUT_ID);
        //then
        Assertions.assertEquals(TAG_WITH_ID, actual);
    }

    @Test
    public void testDeleteByIdShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                tagService.deleteById(ID_INVALID)
        );
    }

    @Test
    public void testDeleteByIdShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                tagService.deleteById(null)
        );
    }

    @Test
    public void testDeleteByIdShouldThrowExceptionWhenTagDoesNotExists() {
        //given
        lenient().when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                tagService.deleteById(ID_VALID)
        );
    }

    @Test
    public void testDeleteByIdShouldDeleteCertificateTags() {
        //given
        //when
        tagService.deleteById(ID_VALID);
        //then
        verify(certificateTagService).deleteByTagId(ID_VALID);
    }

    @Test
    public void testDeleteByIdShouldDeleteTag() {
        //given
        //when
        tagService.deleteById(ID_VALID);
        //then
        verify(tagDao).delete(ID_VALID);
    }

    @Test
    public void testCreateIfNotExistShouldThrowExceptionWhenTagSetIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                tagService.createIfNotExist(null)
        );
    }

    @Test
    public void testCreateIfNotExistShouldThrowExceptionWhenOneOfTagsInvalid() {
        //given
        Tag tagInvalid = new Tag(null, null);
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
    public void testCreateIfNotExistShouldDoNothingWhenTagSetIsEmpty() {
        //given
        //when
        tagService.createIfNotExist(Collections.emptySet());
        //then
        verify(tagDao, never()).findByName(anyString());
        verify(tagDao, never()).create(any());
    }

    @Test
    public void testCreateIfNotExistShouldReturnEmptySetWhenTagSetIsEmpty() {
        //given
        //when
        Set<Tag> actual = tagService.createIfNotExist(Collections.emptySet());
        //then
        Set<Tag> expected = Collections.emptySet();
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void testCreateIfNotExistShouldReturnTagWhenExists() {
        //given
        //when
        Set<Tag> actual = tagService.createIfNotExist(TAG_SET_WITHOUT_ID);
        //then
        Assertions.assertEquals(TAG_SET_WITH_ID, actual);
    }

    @Test
    public void testCreateIfNotExistShouldNotCreateTagWhenExists() {
        //given
        //when
        tagService.createIfNotExist(TAG_SET_WITHOUT_ID);
        //then
        verify(tagDao, never()).create(any());
    }

    @Test
    public void testCreateIfNotExistShouldCreateTagWhenNotFound() {
        //given
        lenient().when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
        //when
        tagService.createIfNotExist(TAG_SET_WITHOUT_ID);
        //then
        verify(tagDao).create(TAG_WITHOUT_ID);
    }

    @Test
    public void testCreateIfNotExistShouldReturnCreatedWhenNotFound() {
        //given
        lenient().when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
        //when
        Set<Tag> actual = tagService.createIfNotExist(TAG_SET_WITHOUT_ID);
        //then
        Assertions.assertEquals(TAG_SET_WITH_ID, actual);
    }

    @Test
    public void testDeleteUnusedShouldDeleteNoCertificateAttachedTags() {
        //given
        //when
        tagService.deleteUnused();
        //then
        verify(tagDao).deleteUnused();
    }

}
