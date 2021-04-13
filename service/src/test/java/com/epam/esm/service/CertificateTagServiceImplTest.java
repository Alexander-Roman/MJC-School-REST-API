package com.epam.esm.service;

import com.epam.esm.persistence.dao.CertificateTagDao;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.anyCollection;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CertificateTagServiceImplTest {

    private static final Long ID_VALID = 1L;
    private static final Long ID_INVALID = -1L;

    private static final Tag TAG_WITH_ID = new Tag(1L, "tagName");
    private static final Set<Tag> TAG_SET_WITH_ID = new HashSet<>(Collections.singletonList(TAG_WITH_ID));

    private static final CertificateTag CERTIFICATE_TAG_WITHOUT_ID = new CertificateTag(null, 1L, 1L);
    private static final CertificateTag CERTIFICATE_TAG_WITH_ID = new CertificateTag(1L, 1L, 1L);

    @Mock
    private CertificateTagDao certificateTagDao;
    @InjectMocks
    private CertificateTagServiceImpl certificateTagService;

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(certificateTagDao.findByCertificateId(anyLong())).thenReturn(Collections.singletonList(CERTIFICATE_TAG_WITH_ID));
    }

    @Test
    public void testAddTagSetShouldThrowExceptionWhenTagSetIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.addTags(ID_VALID, null)
        );
    }

    @Test
    public void testAddTagSetShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateTagService.addTags(ID_INVALID, Collections.emptySet())
        );
    }

    @Test
    public void testAddTagSetShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.addTags(null, Collections.emptySet())
        );
    }

    @Test
    public void testAddTagSetShouldDoNothingWhenTagSetIsEmpty() {
        //given
        //when
        certificateTagService.addTags(ID_VALID, Collections.emptySet());
        //then
        verify(certificateTagDao, never()).create(anyCollection());
    }

    @Test
    public void testAddTagSetShouldCreateCertificateTags() {
        //given
        //when
        certificateTagService.addTags(ID_VALID, TAG_SET_WITH_ID);
        //then
        verify(certificateTagDao).create(Collections.singletonList(CERTIFICATE_TAG_WITHOUT_ID));
    }

    @Test
    public void testUpdateTagSetShouldThrowExceptionWhenTagSetIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.updateTags(ID_VALID, null)
        );
    }

    @Test
    public void testUpdateTagSetShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateTagService.updateTags(ID_INVALID, Collections.emptySet())
        );
    }

    @Test
    public void testUpdateTagSetShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateTagService.updateTags(null, Collections.emptySet())
        );
    }

    @Test
    public void testUpdateTagSetShouldCreteCertificateTagWhenNewTagAdded() {
        //given
        lenient().when(certificateTagDao.findByCertificateId(anyLong())).thenReturn(Collections.emptyList());
        //when
        certificateTagService.updateTags(ID_VALID, TAG_SET_WITH_ID);
        //then
        verify(certificateTagDao).create(Collections.singletonList(CERTIFICATE_TAG_WITHOUT_ID));
    }

    @Test
    public void testUpdateTagSetShouldDeleteCertificateTagWhenTagRemoved() {
        //given
        //when
        certificateTagService.updateTags(ID_VALID, Collections.emptySet());
        //then
        verify(certificateTagDao).delete(Collections.singletonList(ID_VALID));
    }

    @Test
    public void testUpdateTagSetShouldDoNothingWhenNothingToChange() {
        //given
        //when
        certificateTagService.updateTags(ID_VALID, TAG_SET_WITH_ID);
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
        verify(certificateTagDao).deleteByCertificateId(ID_VALID);
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
        verify(certificateTagDao).deleteByTagId(ID_VALID);
    }

}
