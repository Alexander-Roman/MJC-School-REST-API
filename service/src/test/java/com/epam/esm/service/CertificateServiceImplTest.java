package com.epam.esm.service;


import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;


import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CertificateServiceImplTest {
//
//    private static final Long ID_VALID = 1L;
//    private static final Long ID_INVALID = -1L;
//    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
//
//    private static final SortRequest SORT_REQUEST = new SortRequest(
//            Sort.desc("lastUpdateDate"),
//            Sort.desc("createDate")
//    );
//
//    private static final FilterRequest FILTER_REQUEST = new FilterRequest("search", "tagName");
//
//
//    private static final List<Tag> TAGS_WITH_ID = Arrays.asList(
//            new Tag(1L, "tag1", null),
//            new Tag(2L, "tag2", null),
//            new Tag(3L, "tag3", null)
//    );
//
//    private static final List<Tag> TAGS_WITHOUT_ID = Arrays.asList(
//            new Tag(null, "tag1", null),
//            new Tag(null, "tag2", null),
//            new Tag(null, "tag3", null)
//    );
//
//    private static final Certificate CERTIFICATE_WITH_ID = new Certificate(
//            1L,
//            "name",
//            "description",
//            new BigDecimal("42.00"),
//            13,
//            LOCAL_DATE_TIME,
//            LOCAL_DATE_TIME,
//            new HashSet<>(TAGS_WITH_ID)
//    );
//
//    private static final Certificate CERTIFICATE_WITHOUT_ID = new Certificate(
//            null,
//            "name",
//            "description",
//            new BigDecimal("42.00"),
//            13,
//            LOCAL_DATE_TIME,
//            LOCAL_DATE_TIME,
//            new HashSet<>(TAGS_WITHOUT_ID)
//    );
//
//    private static final Certificate CERTIFICATE_NETHER_ID_NO_TAGS = new Certificate(
//            null,
//            "name",
//            "description",
//            new BigDecimal("42.00"),
//            13,
//            LOCAL_DATE_TIME,
//            LOCAL_DATE_TIME,
//            new HashSet<>()
//    );
//
//    private final CertificateRepository certificateRepository = mock(CertificateRepository.class);
//    private final TagService tagService = mock(TagService.class);
//    private final Validator<Certificate> certificateValidator = mock(CertificateValidator.class);
//    private final CertificateSortRequestValidator certificateSortRequestValidator = mock(CertificateSortRequestValidator.class);
//
//    private final CertificateServiceImpl certificateService = new CertificateServiceImpl(
//            certificateRepository,
//            tagService,
//            certificateValidator,
//            certificateSortRequestValidator);
//
//    @BeforeEach
//    public void setUp() {
//        //Positive scenario
//        lenient().when(certificateRepository.findById(anyLong())).thenReturn(Optional.of(CERTIFICATE_WITH_ID));
//        lenient().when(certificateSortRequestValidator.isValid(any())).thenReturn(true);
//        lenient().when(certificateRepository.findAll(any(), any())).thenReturn(Collections.singletonList(CERTIFICATE_WITH_ID));
//        lenient().when(certificateValidator.isValid(any())).thenReturn(true);
//        lenient().when(certificateRepository.save(any())).thenReturn(CERTIFICATE_WITH_ID);
//        lenient().when(tagService.createIfNotExist(any())).thenReturn(new HashSet<>(TAGS_WITH_ID));
//    }
//
//    @Test
//    public void findById_ShouldFindCertificateById() {
//        //given
//        //when
//        Certificate actual = certificateService.findById(ID_VALID);
//        //then
//        Assertions.assertEquals(CERTIFICATE_WITH_ID, actual);
//    }
//
//    @Test
//    public void findById_WhenIdInvalid_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(IllegalArgumentException.class, () ->
//                certificateService.findById(ID_INVALID)
//        );
//    }
//
//    @Test
//    public void findById_WhenIdIsNull_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(NullPointerException.class, () ->
//                certificateService.findById(null)
//        );
//    }
//
//    @Test
//    public void findById_WhenFoundNothing_ShouldThrowException() {
//        //given
//        lenient().when(certificateRepository.findById(ID_VALID)).thenReturn(Optional.empty());
//        //when
//        //then
//        Assertions.assertThrows(EntityNotFoundException.class, () ->
//                certificateService.findById(ID_VALID)
//        );
//    }
//
//    @Test
//    public void findAll_WhenSortRequestIsNull_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(NullPointerException.class, () ->
//                certificateService.findAll(null, FILTER_REQUEST)
//        );
//    }
//
//    @Test
//    public void findAll_WhenFilterRequestIsNull_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(NullPointerException.class, () ->
//                certificateService.findAll(SORT_REQUEST, null)
//        );
//    }
//
//    @Test
//    public void findAll_WhenSortRequestInvalid_ShouldThrowException() {
//        //given
//        lenient().when(certificateSortRequestValidator.isValid(any())).thenReturn(false);
//        //when
//        //then
//        Assertions.assertThrows(ServiceException.class, () ->
//                certificateService.findAll(SORT_REQUEST, FILTER_REQUEST)
//        );
//    }
//
//    @Test
//    public void findAll_ShouldFindListOfCertificates() {
//        //given
//        //when
//        List<Certificate> actual = certificateService.findAll(SORT_REQUEST, FILTER_REQUEST);
//        //then
//        List<Certificate> expected = Collections.singletonList(CERTIFICATE_WITH_ID);
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void create_WhenCertificateIsNull_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(NullPointerException.class, () ->
//                certificateService.create(null)
//        );
//    }
//
//    @Test
//    public void create_ShouldSetActualCreateDate() {
//        //given
//        lenient().when(certificateRepository.save(any())).then(returnsFirstArg());
//        //when
//        Certificate created = certificateService.create(CERTIFICATE_WITHOUT_ID);
//        LocalDateTime actual = created.getCreateDate();
//        //then
//        Assertions.assertTrue(actual.isAfter(LOCAL_DATE_TIME));
//    }
//
//    @Test
//    public void create_ShouldSetActualLastUpdateDate() {
//        //given
//        lenient().when(certificateRepository.save(any())).then(returnsFirstArg());
//        //when
//        Certificate created = certificateService.create(CERTIFICATE_WITHOUT_ID);
//        LocalDateTime actual = created.getLastUpdateDate();
//        //then
//        Assertions.assertTrue(actual.isAfter(LOCAL_DATE_TIME));
//    }
//
//    @Test
//    public void create_WhenCertificateInvalid_ShouldThrowException() {
//        //given
//        lenient().when(certificateValidator.isValid(any())).thenReturn(false);
//        //when
//        //then
//        Assertions.assertThrows(ServiceException.class, () ->
//                certificateService.create(CERTIFICATE_WITHOUT_ID)
//        );
//    }
//
//    @Test
//    public void create_ShouldCreateCertificate() {
//        //given
//        //when
//        certificateService.create(CERTIFICATE_WITHOUT_ID);
//        //then
//        verify(certificateRepository).save(any());
//    }
//
//    @Test
//    public void create_WhenNothingToCreate_ShouldNotCreateTags() {
//        //given
//        //when
//        certificateService.create(CERTIFICATE_NETHER_ID_NO_TAGS);
//        //then
//        verify(tagService, never()).createIfNotExist(any());
//        verify(tagService, never()).create(any());
//    }
//
//    @Test
//    public void create_WhenTagsPresent_ShouldCreateTags() {
//        //given
//        //when
//        certificateService.create(CERTIFICATE_WITHOUT_ID);
//        //then
//        verify(tagService).createIfNotExist(new HashSet<>(TAGS_WITHOUT_ID));
//    }
//
//    @Test
//    public void create_ShouldReturnCertificateCreated() {
//        //given
//        //when
//        Certificate actual = certificateService.create(CERTIFICATE_WITHOUT_ID);
//        //then
//        Assertions.assertEquals(CERTIFICATE_WITH_ID, actual);
//    }
//
//    @Test
//    public void selectiveUpdate_WhenCertificateIsNull_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(NullPointerException.class, () ->
//                certificateService.selectiveUpdate(null)
//        );
//    }
//
//    @Test
//    public void selectiveUpdate_WhenCertificateIdIsNull_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(ServiceException.class, () ->
//                certificateService.selectiveUpdate(CERTIFICATE_WITHOUT_ID)
//        );
//    }
//
//    @Test
//    public void selectiveUpdate_WhenCertificateIdInvalid_ShouldThrowException() {
//        //given
//        Certificate certificateWithIdInvalid = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setId(ID_INVALID)
//                .build();
//        //when
//        //then
//        Assertions.assertThrows(ServiceException.class, () ->
//                certificateService.selectiveUpdate(certificateWithIdInvalid)
//        );
//    }
//
//    @Test
//    public void selectiveUpdate_WhenCertificateDoesNotExists_ShouldThrowException() {
//        //given
//        lenient().when(certificateRepository.findById(anyLong())).thenReturn(Optional.empty());
//        //when
//        //then
//        Assertions.assertThrows(EntityNotFoundException.class, () ->
//                certificateService.selectiveUpdate(CERTIFICATE_WITH_ID)
//        );
//    }
//
//    @Test
//    public void selectiveUpdate_WhenOnlyNameUpdated_ShouldValidateCertificate() {
//        //given
//        Certificate updateName = new Certificate.Builder()
//                .setId(ID_VALID)
//                .setName("Updated name")
//                .build();
//        //when
//        certificateService.selectiveUpdate(updateName);
//        //then
//        Certificate expected = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setName("Updated name")
//                .build();
//        verify(certificateValidator).isValid(expected);
//    }
//
//    @Test
//    public void selectiveUpdate_WhenOnlyDescriptionUpdated_ShouldValidateCertificate() {
//        //given
//        Certificate updateDescription = new Certificate.Builder()
//                .setId(ID_VALID)
//                .setDescription("Updated description")
//                .build();
//        //when
//        certificateService.selectiveUpdate(updateDescription);
//        //then
//        Certificate expected = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setDescription("Updated description")
//                .build();
//        verify(certificateValidator).isValid(expected);
//    }
//
//    @Test
//    public void selectiveUpdate_WhenOnlyPriceUpdated_ShouldValidateCertificate() {
//        //given
//        Certificate updatePrice = new Certificate.Builder()
//                .setId(ID_VALID)
//                .setPrice(new BigDecimal("10.00"))
//                .build();
//        //when
//        certificateService.selectiveUpdate(updatePrice);
//        //then
//        Certificate expected = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setPrice(new BigDecimal("10.00"))
//                .build();
//        verify(certificateValidator).isValid(expected);
//    }
//
//    @Test
//    public void selectiveUpdate_WhenOnlyDurationUpdated_ShouldValidateCertificate() {
//        //given
//        Certificate updateDuration = new Certificate.Builder()
//                .setId(ID_VALID)
//                .setDuration(1)
//                .build();
//        //when
//        certificateService.selectiveUpdate(updateDuration);
//        //then
//        Certificate expected = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setDuration(1)
//                .build();
//        verify(certificateValidator).isValid(expected);
//    }
//
//    @Test
//    public void selectiveUpdate_WhenOnlyTagsUpdated_ShouldValidateCertificate() {
//        //given
//        Certificate updateTags = new Certificate.Builder()
//                .setId(ID_VALID)
//                .setTags(new HashSet<>(TAGS_WITHOUT_ID))
//                .build();
//        //when
//        certificateService.selectiveUpdate(updateTags);
//        //then
//        Certificate expected = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setTags(new HashSet<>(TAGS_WITHOUT_ID))
//                .build();
//        verify(certificateValidator).isValid(expected);
//    }
//
//    @Test
//    public void selectiveUpdate_ShouldValidateCertificateWithoutCreateDateUpdate() {
//        //given
//        LocalDateTime createDate = LocalDateTime.now();
//        Certificate updateCreateDate = new Certificate.Builder()
//                .setId(ID_VALID)
//                .setCreateDate(createDate)
//                .build();
//        //when
//        certificateService.selectiveUpdate(updateCreateDate);
//        //then
//        Certificate unexpected = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setCreateDate(createDate)
//                .build();
//        verify(certificateValidator, never()).isValid(unexpected);
//    }
//
//    @Test
//    public void selectiveUpdate_ShouldValidateCertificateWithoutLastUpdateDateUpdate() {
//        //given
//        LocalDateTime lastUpdateDate = LocalDateTime.now();
//        Certificate updateCreateDate = new Certificate.Builder()
//                .setId(ID_VALID)
//                .setLastUpdateDate(lastUpdateDate)
//                .build();
//        //when
//        certificateService.selectiveUpdate(updateCreateDate);
//        //then
//        Certificate unexpected = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setLastUpdateDate(lastUpdateDate)
//                .build();
//        verify(certificateValidator, never()).isValid(unexpected);
//    }
//
//    @Test
//    public void selectiveUpdate_WhenUpdatedCertificateInvalid_ShouldThrowException() {
//        //given
//        lenient().when(certificateValidator.isValid(any())).thenReturn(false);
//        //when
//        //then
//        Assertions.assertThrows(ServiceException.class, () ->
//                certificateService.selectiveUpdate(CERTIFICATE_WITH_ID)
//        );
//    }
//
//    @Test
//    public void selectiveUpdate_ShouldSetActualLastUpdateDate() {
//        //given
//        lenient().when(certificateRepository.save(any())).then(returnsFirstArg());
//        //when
//        Certificate updated = certificateService.selectiveUpdate(CERTIFICATE_WITH_ID);
//        LocalDateTime actual = updated.getLastUpdateDate();
//        //then
//        Assertions.assertTrue(actual.isAfter(LOCAL_DATE_TIME));
//    }
//
//    @Test
//    public void selectiveUpdate_ShouldReceiveConsistentTags() {
//        //given
//        Certificate tagsWithoutId = Certificate.Builder
//                .from(CERTIFICATE_WITH_ID)
//                .setTags(new HashSet<>(TAGS_WITHOUT_ID))
//                .build();
//        //when
//        Certificate updated = certificateService.selectiveUpdate(tagsWithoutId);
//        Set<Tag> actual = updated.getTags();
//        //then
//        Set<Tag> expected = new HashSet<>(TAGS_WITH_ID);
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    public void selectiveUpdate_ShouldCreateNewTags() {
//        //given
//        //when
//        Certificate updated = certificateService.selectiveUpdate(CERTIFICATE_WITH_ID);
//        //then
//        verify(tagService).createIfNotExist(new HashSet<>(TAGS_WITH_ID));
//    }
//
//    @Test
//    public void deleteById_WhenIdInvalid_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(IllegalArgumentException.class, () ->
//                certificateService.deleteById(ID_INVALID)
//        );
//    }
//
//    @Test
//    public void deleteById_WhenIdIsNull_ShouldThrowException() {
//        //given
//        //when
//        //then
//        Assertions.assertThrows(NullPointerException.class, () ->
//                certificateService.deleteById(null)
//        );
//    }
//
//    @Test
//    public void deleteById_WhenCertificateDoesNotExists_ShouldThrowException() {
//        //given
//        lenient().when(certificateRepository.findById(anyLong())).thenReturn(Optional.empty());
//        //when
//        //then
//        Assertions.assertThrows(EntityNotFoundException.class, () ->
//                certificateService.deleteById(ID_VALID)
//        );
//    }
//
//    @Test
//    public void deleteById_ShouldDeleteCertificate() {
//        //given
//        //when
//        certificateService.deleteById(ID_VALID);
//        //then
//        verify(certificateRepository).delete(1L);
//    }

}
