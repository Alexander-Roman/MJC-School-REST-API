package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateTagDao;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.entity.Tag;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CertificateTagServiceImplTest {

    private static final Long ID_VALID = 1L;
    private static final Long ID_INVALID = -1L;

    private static final Tag TAG_WITH_ID = new Tag(1L, "tagName");
    private static final Tag TAG_WITHOUT_ID = new Tag(null, "tagName");
    private static final Set<Tag> TAG_SET_WITH_ID = new HashSet<>(Collections.singletonList(TAG_WITH_ID));

    private static final CertificateTag CERTIFICATE_TAG_WITHOUT_ID = new CertificateTag(null, 1L, 1L);
    private static final CertificateTag CERTIFICATE_TAG_WITH_ID = new CertificateTag(1L, 1L, 1L);

    @Mock
    private CertificateTagDao certificateTagDao;
    @Mock
    private Validator<CertificateTag> certificateTagValidator;
    @InjectMocks
    private CertificateTagServiceImpl certificateTagService;

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(certificateTagValidator.isValid(any())).thenReturn(true);
        lenient().when(certificateTagDao.findByCertificateId(anyLong())).thenReturn(Collections.singletonList(CERTIFICATE_TAG_WITH_ID));
    }

    @Test
    public void testAddTagSetShouldThrowExceptionWhenTagSetIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.addTagSet(ID_VALID, null)
        );
    }

    @Test
    public void testAddTagSetShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateTagService.addTagSet(ID_INVALID, Collections.emptySet())
        );
    }

    @Test
    public void testAddTagSetShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.addTagSet(null, Collections.emptySet())
        );
    }

    @Test
    public void testAddTagSetShouldDoNothingWhenTagSetIsEmpty() {
        //given
        //when
        certificateTagService.addTagSet(ID_VALID, Collections.emptySet());
        //then
        verify(certificateTagDao, never()).create(anyCollection());
    }

    @Test
    public void testAddTagSetShouldThrowExceptionWhenOneOfCreatedCertificateTagsInvalid() {
        //given
        Set<Tag> tags = new HashSet<>(Arrays.asList(TAG_WITH_ID, TAG_WITHOUT_ID));
        CertificateTag invalid = new CertificateTag(null, ID_VALID, null);
        lenient().when(certificateTagValidator.isValid(invalid)).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateTagService.addTagSet(ID_VALID, tags)
        );
    }

    @Test
    public void testAddTagSetShouldCreateCertificateTags() {
        //given
        //when
        certificateTagService.addTagSet(ID_VALID, TAG_SET_WITH_ID);
        //then
        verify(certificateTagDao, times(1)).create(Collections.singletonList(CERTIFICATE_TAG_WITHOUT_ID));
    }

    @Test
    public void testUpdateTagSetShouldThrowExceptionWhenTagSetIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.updateTagSet(ID_VALID, null)
        );
    }

    @Test
    public void testUpdateTagSetShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateTagService.updateTagSet(ID_INVALID, Collections.emptySet())
        );
    }

    @Test
    public void testUpdateTagSetShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.updateTagSet(null, Collections.emptySet())
        );
    }

    @Test
    public void testUpdateTagSetShouldThrowExceptionWhenOneOfCreatedCertificateTagsInvalid() {
        //given
        Set<Tag> tags = new HashSet<>(Arrays.asList(TAG_WITH_ID, TAG_WITHOUT_ID));
        CertificateTag invalid = new CertificateTag(null, ID_VALID, null);
        lenient().when(certificateTagValidator.isValid(invalid)).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateTagService.updateTagSet(ID_VALID, tags)
        );
    }

    @Test
    public void testUpdateTagSetShouldCreteCertificateTagWhenNewTagAdded() {
        //given
        lenient().when(certificateTagDao.findByCertificateId(anyLong())).thenReturn(Collections.emptyList());
        //when
        certificateTagService.updateTagSet(ID_VALID, TAG_SET_WITH_ID);
        //then
        verify(certificateTagDao, times(1)).create(Collections.singletonList(CERTIFICATE_TAG_WITHOUT_ID));
    }

    @Test
    public void testUpdateTagSetShouldDeleteCertificateTagWhenTagRemoved() {
        //given
        //when
        certificateTagService.updateTagSet(ID_VALID, Collections.emptySet());
        //then
        verify(certificateTagDao, times(1)).delete(Collections.singletonList(ID_VALID));
    }

    @Test
    public void testUpdateTagSetShouldDoNothingWhenNothingToChange() {
        //given
        //when
        certificateTagService.updateTagSet(ID_VALID, TAG_SET_WITH_ID);
        //then
        verify(certificateTagDao, never()).create(anyCollection());
        verify(certificateTagDao, never()).delete(anyCollection());
    }

    @Test
    public void testDeleteByCertificateIdShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateTagService.deleteByCertificateId(ID_INVALID)
        );
    }

    @Test
    public void testDeleteByCertificateIdShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.deleteByCertificateId(null)
        );
    }

    @Test
    public void testDeleteByCertificateIdShouldDeleteCertificateTags() {
        //given
        //when
        certificateTagService.deleteByCertificateId(ID_VALID);
        //then
        verify(certificateTagDao, times(1)).deleteByCertificateId(ID_VALID);
    }

    @Test
    public void testDeleteByTagIdShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateTagService.deleteByTagId(ID_INVALID)
        );
    }

    @Test
    public void testDeleteByTagIdShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.deleteByTagId(null)
        );
    }

    @Test
    public void testDeleteByTagIdShouldDeleteCertificateTags() {
        //given
        //when
        certificateTagService.deleteByTagId(ID_VALID);
        //then
        verify(certificateTagDao, times(1)).deleteByTagId(ID_VALID);
    }

}
