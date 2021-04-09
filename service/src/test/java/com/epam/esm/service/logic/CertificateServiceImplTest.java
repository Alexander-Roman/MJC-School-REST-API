package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateDao;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.Sort;
import com.epam.esm.persistence.model.SortRequest;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.CertificateSortRequestValidator;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CertificateServiceImplTest {

    private static final Long ID_VALID = 1L;
    private static final Long ID_INVALID = -1L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

    private static final SortRequest SORT_REQUEST = new SortRequest(
            Sort.desc("lastUpdateDate"),
            Sort.desc("createDate")
    );

    private static final FilterRequest FILTER_REQUEST = new FilterRequest("search", "tagName");


    private static final List<Tag> TAGS_WITH_ID = Arrays.asList(
            new Tag(1L, "tag1"),
            new Tag(2L, "tag2"),
            new Tag(3L, "tag3")
    );

    private static final List<Tag> TAGS_WITHOUT_ID = Arrays.asList(
            new Tag(null, "tag1"),
            new Tag(null, "tag2"),
            new Tag(null, "tag3")
    );

    private static final Certificate CERTIFICATE_WITH_ID = new Certificate(
            1L,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LOCAL_DATE_TIME,
            LOCAL_DATE_TIME,
            new HashSet<>(TAGS_WITH_ID)
    );

    private static final Certificate CERTIFICATE_WITHOUT_ID = new Certificate(
            null,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LOCAL_DATE_TIME,
            LOCAL_DATE_TIME,
            new HashSet<>(TAGS_WITHOUT_ID)
    );

    private static final Certificate CERTIFICATE_NETHER_ID_NO_TAGS = new Certificate(
            null,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LOCAL_DATE_TIME,
            LOCAL_DATE_TIME,
            new HashSet<>()
    );

    private final CertificateDao certificateDao = mock(CertificateDao.class);
    private final TagService tagService = mock(TagService.class);
    private final CertificateTagService certificateTagService = mock(CertificateTagService.class);
    private final Validator<Certificate> certificateValidator = mock(CertificateValidator.class);
    private final CertificateSortRequestValidator certificateSortRequestValidator = mock(CertificateSortRequestValidator.class);

    private final CertificateServiceImpl certificateService = new CertificateServiceImpl(
            certificateDao,
            tagService,
            certificateTagService,
            certificateValidator,
            certificateSortRequestValidator
    );

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(certificateDao.findById(anyLong())).thenReturn(Optional.of(CERTIFICATE_WITH_ID));
        lenient().when(certificateSortRequestValidator.isValid(any())).thenReturn(true);
        lenient().when(certificateDao.findAll(any(), any())).thenReturn(Collections.singletonList(CERTIFICATE_WITH_ID));
        lenient().when(certificateValidator.isValid(any())).thenReturn(true);
        lenient().when(certificateDao.create(any())).thenReturn(CERTIFICATE_WITH_ID);
        lenient().when(tagService.createIfNotExist(any())).thenReturn(new HashSet<>(TAGS_WITH_ID));
        lenient().when(certificateDao.update(any())).then(returnsFirstArg());
    }

    @Test
    public void testFindByIdShouldFindCertificateById() {
        //given
        //when
        Certificate actual = certificateService.findById(ID_VALID);
        //then
        Assertions.assertEquals(CERTIFICATE_WITH_ID, actual);
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateService.findById(ID_INVALID)
        );
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.findById(null)
        );
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenFoundNothing() {
        //given
        lenient().when(certificateDao.findById(ID_VALID)).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                certificateService.findById(ID_VALID)
        );
    }

    @Test
    public void testFindAllShouldThrowExceptionWhenSortRequestIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.findAll(null, FILTER_REQUEST)
        );
    }

    @Test
    public void testFindAllShouldThrowExceptionWhenFilterRequestIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.findAll(SORT_REQUEST, null)
        );
    }

    @Test
    public void testFindAllShouldThrowExceptionWhenSortRequestInvalid() {
        //given
        lenient().when(certificateSortRequestValidator.isValid(any())).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.findAll(SORT_REQUEST, FILTER_REQUEST)
        );
    }

    @Test
    public void testFindAllShouldFindListOfCertificates() {
        //given
        //when
        List<Certificate> actual = certificateService.findAll(SORT_REQUEST, FILTER_REQUEST);
        //then
        List<Certificate> expected = Collections.singletonList(CERTIFICATE_WITH_ID);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCreateShouldThrowExceptionWhenCertificateIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.create(null)
        );
    }

    @Test
    public void testCreateShouldThrowExceptionWhenCertificateIdSpecified() {
        //given
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.create(CERTIFICATE_WITH_ID)
        );
    }

    @Test
    public void testCreateShouldSetActualCreateDate() {
        //given
        lenient().when(certificateDao.create(any())).then(returnsFirstArg());
        //when
        Certificate created = certificateService.create(CERTIFICATE_WITHOUT_ID);
        LocalDateTime actual = created.getCreateDate();
        //then
        Assertions.assertTrue(actual.isAfter(LOCAL_DATE_TIME));
    }

    @Test
    public void testCreateShouldSetActualLastUpdateDate() {
        //given
        lenient().when(certificateDao.create(any())).then(returnsFirstArg());
        //when
        Certificate created = certificateService.create(CERTIFICATE_WITHOUT_ID);
        LocalDateTime actual = created.getLastUpdateDate();
        //then
        Assertions.assertTrue(actual.isAfter(LOCAL_DATE_TIME));
    }

    @Test
    public void testCreateShouldThrowExceptionWhenCertificateInvalid() {
        //given
        lenient().when(certificateValidator.isValid(any())).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.create(CERTIFICATE_WITHOUT_ID)
        );
    }

    @Test
    public void testCreateShouldCreateCertificate() {
        //given
        //when
        certificateService.create(CERTIFICATE_WITHOUT_ID);
        //then
        verify(certificateDao, times(1)).create(any());
    }

    @Test
    public void testCreateShouldNotCreateTagsWhenNothingToCreate() {
        //given
        //when
        certificateService.create(CERTIFICATE_NETHER_ID_NO_TAGS);
        //then
        verify(tagService, never()).createIfNotExist(any());
        verify(tagService, never()).create(any());
    }

    @Test
    public void testCreateShouldNotAddTagsWhenNothingToAdd() {
        //given
        //when
        certificateService.create(CERTIFICATE_NETHER_ID_NO_TAGS);
        //then
        verify(certificateTagService, never()).addTagSet(anyLong(), anySet());
        verify(certificateTagService, never()).updateTagSet(anyLong(), anySet());
    }

    @Test
    public void testCreateShouldCreateTagsWhenNecessary() {
        //given
        //when
        certificateService.create(CERTIFICATE_WITHOUT_ID);
        //then
        verify(tagService, times(1)).createIfNotExist(new HashSet<>(TAGS_WITHOUT_ID));
    }

    @Test
    public void testCreateShouldAddTagsWhenNecessary() {
        //given
        //when
        certificateService.create(CERTIFICATE_WITHOUT_ID);
        //then
        verify(certificateTagService, times(1)).addTagSet(anyLong(), eq(new HashSet<>(TAGS_WITH_ID)));
    }

    @Test
    public void testCreateShouldReturnCertificateCreated() {
        //given
        //when
        Certificate actual = certificateService.create(CERTIFICATE_WITHOUT_ID);
        //then
        Assertions.assertEquals(CERTIFICATE_WITH_ID, actual);
    }

    @Test
    public void testSelectiveUpdateShouldThrowExceptionWhenCertificateIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.selectiveUpdate(null)
        );
    }

    @Test
    public void testSelectiveUpdateShouldThrowExceptionWhenCertificateIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.selectiveUpdate(CERTIFICATE_WITHOUT_ID)
        );
    }

    @Test
    public void testSelectiveUpdateShouldThrowExceptionWhenCertificateIdInvalid() {
        //given
        Certificate certificateWithIdInvalid = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setId(ID_INVALID)
                .build();
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.selectiveUpdate(certificateWithIdInvalid)
        );
    }

    @Test
    public void testSelectiveUpdateShouldThrowExceptionWhenCertificateDoesNotExists() {
        //given
        lenient().when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                certificateService.selectiveUpdate(CERTIFICATE_WITH_ID)
        );
    }

    @Test
    public void testSelectiveUpdateShouldValidateCertificateWhenOnlyNameUpdated() {
        //given
        Certificate updateName = new Certificate.Builder()
                .setId(ID_VALID)
                .setName("Updated name")
                .build();
        //when
        certificateService.selectiveUpdate(updateName);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setName("Updated name")
                .build();
        verify(certificateValidator, times(1)).isValid(expected);
    }

    @Test
    public void testSelectiveUpdateShouldValidateCertificateWhenOnlyDescriptionUpdated() {
        //given
        Certificate updateDescription = new Certificate.Builder()
                .setId(ID_VALID)
                .setDescription("Updated description")
                .build();
        //when
        certificateService.selectiveUpdate(updateDescription);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setDescription("Updated description")
                .build();
        verify(certificateValidator, times(1)).isValid(expected);
    }

    @Test
    public void testSelectiveUpdateShouldValidateCertificateWhenOnlyPriceUpdated() {
        //given
        Certificate updatePrice = new Certificate.Builder()
                .setId(ID_VALID)
                .setPrice(new BigDecimal("10.00"))
                .build();
        //when
        certificateService.selectiveUpdate(updatePrice);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setPrice(new BigDecimal("10.00"))
                .build();
        verify(certificateValidator, times(1)).isValid(expected);
    }

    @Test
    public void testSelectiveUpdateShouldValidateCertificateWhenOnlyDurationUpdated() {
        //given
        Certificate updateDuration = new Certificate.Builder()
                .setId(ID_VALID)
                .setDuration(1)
                .build();
        //when
        certificateService.selectiveUpdate(updateDuration);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setDuration(1)
                .build();
        verify(certificateValidator, times(1)).isValid(expected);
    }

    @Test
    public void testSelectiveUpdateShouldValidateCertificateWhenOnlyTagsUpdated() {
        //given
        Certificate updateTags = new Certificate.Builder()
                .setId(ID_VALID)
                .setTags(new HashSet<>(TAGS_WITHOUT_ID))
                .build();
        //when
        certificateService.selectiveUpdate(updateTags);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setTags(new HashSet<>(TAGS_WITHOUT_ID))
                .build();
        verify(certificateValidator, times(1)).isValid(expected);
    }

    @Test
    public void testSelectiveUpdateShouldValidateCertificateWithoutCreateDateUpdate() {
        //given
        LocalDateTime createDate = LocalDateTime.now();
        Certificate updateCreateDate = new Certificate.Builder()
                .setId(ID_VALID)
                .setCreateDate(createDate)
                .build();
        //when
        certificateService.selectiveUpdate(updateCreateDate);
        //then
        Certificate unexpected = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setCreateDate(createDate)
                .build();
        verify(certificateValidator, never()).isValid(unexpected);
    }

    @Test
    public void testSelectiveUpdateShouldValidateCertificateWithoutLastUpdateDateUpdate() {
        //given
        LocalDateTime lastUpdateDate = LocalDateTime.now();
        Certificate updateCreateDate = new Certificate.Builder()
                .setId(ID_VALID)
                .setLastUpdateDate(lastUpdateDate)
                .build();
        //when
        certificateService.selectiveUpdate(updateCreateDate);
        //then
        Certificate unexpected = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setLastUpdateDate(lastUpdateDate)
                .build();
        verify(certificateValidator, never()).isValid(unexpected);
    }

    @Test
    public void testSelectiveUpdateShouldThrowExceptionWhenUpdatedCertificateInvalid() {
        //given
        lenient().when(certificateValidator.isValid(any())).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.selectiveUpdate(CERTIFICATE_WITH_ID)
        );
    }

    @Test
    public void testSelectiveUpdateShouldSetActualLastUpdateDate() {
        //given
        //when
        Certificate updated = certificateService.selectiveUpdate(CERTIFICATE_WITH_ID);
        LocalDateTime actual = updated.getLastUpdateDate();
        //then
        Assertions.assertTrue(actual.isAfter(LOCAL_DATE_TIME));
    }

    @Test
    public void testSelectiveUpdateShouldReceiveConsistentTags() {
        //given
        Certificate tagsWithoutId = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setTags(new HashSet<>(TAGS_WITHOUT_ID))
                .build();
        //when
        Certificate updated = certificateService.selectiveUpdate(tagsWithoutId);
        Set<Tag> actual = updated.getTags();
        //then
        Set<Tag> expected = new HashSet<>(TAGS_WITH_ID);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testSelectiveUpdateShouldCreateNewTags() {
        //given
        //when
        Certificate updated = certificateService.selectiveUpdate(CERTIFICATE_WITH_ID);
        //then
        verify(tagService, times(1)).createIfNotExist(new HashSet<>(TAGS_WITH_ID));
    }

    @Test
    public void testSelectiveUpdateShouldUpdateCertificateTags() {
        //given
        //when
        Certificate updated = certificateService.selectiveUpdate(CERTIFICATE_WITH_ID);
        //then
        verify(certificateTagService, times(1)).updateTagSet(ID_VALID, new HashSet<>(TAGS_WITH_ID));
    }

    @Test
    public void testDeleteByIdShouldThrowExceptionWhenIdInvalid() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateService.deleteById(ID_INVALID)
        );
    }

    @Test
    public void testDeleteByIdShouldThrowExceptionWhenIdIsNull() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.deleteById(null)
        );
    }

    @Test
    public void testDeleteByIdShouldThrowExceptionWhenCertificateDoesNotExists() {
        //given
        lenient().when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                certificateService.deleteById(ID_VALID)
        );
    }

    @Test
    public void testDeleteByIdShouldDeleteCertificateTags() {
        //given
        //when
        certificateService.deleteById(ID_VALID);
        //then
        verify(certificateTagService, times(1)).deleteByCertificateId(ID_VALID);
    }

    @Test
    public void testDeleteByIdShouldDeleteCertificate() {
        //given
        //when
        certificateService.deleteById(ID_VALID);
        //then
        verify(certificateDao, times(1)).delete(ID_VALID);
    }

}
