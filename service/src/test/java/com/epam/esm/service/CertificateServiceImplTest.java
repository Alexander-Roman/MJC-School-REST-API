package com.epam.esm.service;


import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.repository.CertificateRepository;
import com.epam.esm.persistence.specification.FindAllSpecification;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.service.validator.CertificateValidator;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CertificateServiceImplTest {

    private static final Long ID_VALID = 1L;
    private static final Long ID_INVALID = -1L;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

    private static final List<Tag> TAGS_WITH_ID = Arrays.asList(
            new Tag(1L, "tag1", null),
            new Tag(2L, "tag2", null),
            new Tag(3L, "tag3", null)
    );

    private static final List<Tag> TAGS_WITHOUT_ID = Arrays.asList(
            new Tag(null, "tag1", null),
            new Tag(null, "tag2", null),
            new Tag(null, "tag3", null)
    );

    private static final Certificate CERTIFICATE_WITH_ID = new Certificate(
            1L,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LOCAL_DATE_TIME,
            LOCAL_DATE_TIME,
            new HashSet<>(TAGS_WITH_ID),
            false
    );

    private static final Certificate CERTIFICATE_WITHOUT_ID = new Certificate(
            null,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LOCAL_DATE_TIME,
            LOCAL_DATE_TIME,
            new HashSet<>(TAGS_WITHOUT_ID),
            false
    );

    private static final Certificate CERTIFICATE_NETHER_ID_NO_TAGS = new Certificate(
            null,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LOCAL_DATE_TIME,
            LOCAL_DATE_TIME,
            new HashSet<>(),
            false
    );

    private final CertificateRepository certificateRepository = mock(CertificateRepository.class);
    private final TagService tagService = mock(TagService.class);
    private final Validator<Certificate> certificateValidator = mock(CertificateValidator.class);
    private final CertificateMapper certificateMapper = mock(CertificateMapper.class);

    private final CertificateServiceImpl certificateService = new CertificateServiceImpl(
            certificateRepository,
            tagService,
            certificateValidator,
            certificateMapper);

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(certificateRepository.findById(anyLong())).thenReturn(Optional.of(CERTIFICATE_WITH_ID));
        lenient().when(certificateRepository.findSingle(any())).thenReturn(Optional.of(CERTIFICATE_WITH_ID));
        lenient().when(certificateRepository.find(any(), any())).thenReturn(new PageImpl<>(Collections.singletonList(CERTIFICATE_WITH_ID)));
        lenient().when(certificateValidator.isValid(any())).thenReturn(true);
        lenient().when(certificateRepository.save(any())).thenReturn(CERTIFICATE_WITH_ID);
        lenient().when(tagService.createIfNotExist(any())).thenReturn(new HashSet<>(TAGS_WITH_ID));
        lenient().when(certificateMapper.merge(any(), any())).thenReturn(CERTIFICATE_WITH_ID);
    }

    @Test
    public void findById_ShouldFindCertificateById() {
        //given
        //when
        Certificate actual = certificateService.findById(ID_VALID);
        //then
        Assertions.assertEquals(CERTIFICATE_WITH_ID, actual);
    }

    @Test
    public void findById_WhenIdInvalid_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateService.findById(ID_INVALID)
        );
    }

    @Test
    public void findById_WhenIdIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.findById(null)
        );
    }

    @Test
    public void findById_WhenFoundNothing_ShouldThrowException() {
        //given
        lenient().when(certificateRepository.findSingle(any())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                certificateService.findById(ID_VALID)
        );
    }

    @Test
    public void findPage_WhenPageableIsNull_ShouldThrowException() {
        //given
        Specification<Certificate> specification = new FindAllSpecification<>();
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.findPage(null, specification)
        );
    }

    @Test
    public void findPage_WhenSpecificationIsNull_ShouldThrowException() {
        //given
        Pageable pageable = PageRequest.of(1, 10, Sort.unsorted());
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.findPage(pageable, null)
        );
    }

    @Test
    public void findPage_ShouldFindPageOfPurchases() {
        //given
        Pageable pageable = PageRequest.of(1, 10, Sort.unsorted());
        Specification<Certificate> specification = new FindAllSpecification<>();
        //when
        certificateService.findPage(pageable, specification);
        //then
        verify(certificateRepository).find(pageable, specification);
    }

    @Test
    public void create_WhenCertificateIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.create(null)
        );
    }

    @Test
    public void create_WhenCertificateInvalid_ShouldThrowException() {
        //given
        lenient().when(certificateValidator.isValid(any())).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.create(CERTIFICATE_WITHOUT_ID)
        );
    }

    @Test
    public void create_ShouldCreateCertificate() {
        //given
        //when
        certificateService.create(CERTIFICATE_WITHOUT_ID);
        //then
        verify(certificateRepository).save(any());
    }

    @Test
    public void create_WhenNothingToCreate_ShouldNotCreateTags() {
        //given
        //when
        certificateService.create(CERTIFICATE_NETHER_ID_NO_TAGS);
        //then
        verify(tagService, never()).createIfNotExist(any());
        verify(tagService, never()).create(any());
    }

    @Test
    public void create_WhenTagsPresent_ShouldCreateTags() {
        //given
        //when
        certificateService.create(CERTIFICATE_WITHOUT_ID);
        //then
        verify(tagService).createIfNotExist(new HashSet<>(TAGS_WITHOUT_ID));
    }

    @Test
    public void create_ShouldReturnCertificateCreated() {
        //given
        //when
        Certificate actual = certificateService.create(CERTIFICATE_WITHOUT_ID);
        //then
        Assertions.assertEquals(CERTIFICATE_WITH_ID, actual);
    }

    @Test
    public void selectiveUpdate_WhenCertificateIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.selectiveUpdate(null)
        );
    }

    @Test
    public void selectiveUpdate_WhenCertificateIdIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.selectiveUpdate(CERTIFICATE_WITHOUT_ID)
        );
    }

    @Test
    public void selectiveUpdate_WhenCertificateIdInvalid_ShouldThrowException() {
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
    public void selectiveUpdate_WhenCertificateDoesNotExists_ShouldThrowException() {
        //given
        lenient().when(certificateRepository.findSingle(any())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                certificateService.selectiveUpdate(CERTIFICATE_WITH_ID)
        );
    }

    @Test
    public void selectiveUpdate_ShouldMergeUpdates() {
        //given
        Certificate source = new Certificate.Builder()
                .setId(ID_VALID)
                .setName("Updated name")
                .build();
        //when
        certificateService.selectiveUpdate(source);
        //then
        verify(certificateMapper).merge(source, CERTIFICATE_WITH_ID);
    }

    @Test
    public void selectiveUpdate_WhenUpdatedCertificateInvalid_ShouldThrowException() {
        //given
        lenient().when(certificateValidator.isValid(any())).thenReturn(false);
        //when
        //then
        Assertions.assertThrows(ServiceException.class, () ->
                certificateService.selectiveUpdate(CERTIFICATE_WITH_ID)
        );
    }

    @Test
    public void selectiveUpdate_ShouldReceiveConsistentTags() {
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
    public void selectiveUpdate_ShouldCreateNewTags() {
        //given
        //when
        Certificate updated = certificateService.selectiveUpdate(CERTIFICATE_WITH_ID);
        //then
        verify(tagService).createIfNotExist(new HashSet<>(TAGS_WITH_ID));
    }

    @Test
    public void deleteById_WhenIdInvalid_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                certificateService.deleteById(ID_INVALID)
        );
    }

    @Test
    public void deleteById_WhenIdIsNull_ShouldThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () ->
                certificateService.deleteById(null)
        );
    }

    @Test
    public void deleteById_WhenCertificateDoesNotExists_ShouldThrowException() {
        //given
        lenient().when(certificateRepository.findSingle(any())).thenReturn(Optional.empty());
        //when
        //then
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                certificateService.deleteById(ID_VALID)
        );
    }

    @Test
    public void deleteById_ShouldPerformSoftDelete() {
        //given
        Certificate deleted = Certificate.Builder
                .from(CERTIFICATE_WITH_ID)
                .setDeleted(true)
                .build();
        //when
        certificateService.deleteById(ID_VALID);
        //then
        verify(certificateRepository).save(deleted);
        verify(certificateRepository, never()).delete(1L);
    }

}
