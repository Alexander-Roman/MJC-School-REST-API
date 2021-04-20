package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.config.TestPersistenceConfig;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.init.TestApplication;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.Sort;
import com.epam.esm.persistence.model.SortRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {TestPersistenceConfig.class})
@ActiveProfiles("integrationTest")
@Transactional
public class CertificateRepositoryImplTest {

    private static final LocalDateTime LOCAL_DATE_TIME_TEST = LocalDateTime.parse("2021-01-01T12:00:00");
    private static final List<Tag> TAGS_FIRST = Arrays.asList(
            new Tag(1L, "tag1", null),
            new Tag(2L, "tag2", null)
    );
    private static final List<Tag> TAGS_SECOND = Collections.singletonList(
            new Tag(2L, "tag2", null)
    );
    private static final Certificate FIRST = new Certificate(
            1L,
            "certificate number 1",
            "description number 2",
            new BigDecimal("33.33"),
            5,
            LocalDateTime.parse("2020-05-05T05:55:55"),
            LocalDateTime.parse("2021-01-01T01:11:11"),
            new HashSet<>(TAGS_FIRST)
    );
    private static final Certificate SECOND = new Certificate(
            2L,
            "certificate number 2",
            "description number 3",
            new BigDecimal("44.44"),
            5,
            LocalDateTime.parse("2020-01-01T01:11:11"),
            LocalDateTime.parse("2021-02-02T02:22:22"),
            new HashSet<>(TAGS_SECOND)
    );
    private static final Certificate THIRD = new Certificate(
            3L,
            "certificate number 3",
            "description number 4",
            new BigDecimal("55.55"),
            10,
            LocalDateTime.parse("2020-02-02T02:22:22"),
            LocalDateTime.parse("2021-03-03T03:33:33"),
            Collections.emptySet()
    );
    private static final Certificate FOURTH = new Certificate(
            4L,
            "certificate number 4",
            "description number 5",
            new BigDecimal("11.11"),
            10,
            LocalDateTime.parse("2020-03-03T03:33:33"),
            LocalDateTime.parse("2021-04-04T04:44:44"),
            Collections.emptySet()
    );
    private static final Certificate FIFTH = new Certificate(
            5L,
            "certificate number 5",
            "description number 1",
            new BigDecimal("22.22"),
            15,
            LocalDateTime.parse("2020-04-04T04:44:44"),
            LocalDateTime.parse("2021-05-05T05:55:55"),
            Collections.emptySet()
    );
    private static final Certificate FIFTH_TO_UPDATE = new Certificate(
            5L,
            "certificate name updated",
            "certificate description updated",
            new BigDecimal("42.00"),
            42,
            LOCAL_DATE_TIME_TEST,
            LOCAL_DATE_TIME_TEST,
            Collections.emptySet()
    );
    private static final Certificate CERTIFICATE_TO_CREATE = new Certificate(
            null,
            "certificate name",
            "certificate description",
            new BigDecimal("42.00"),
            42,
            LOCAL_DATE_TIME_TEST,
            LOCAL_DATE_TIME_TEST,
            Collections.emptySet()
    );

    private final CertificateRepository certificateRepository;

    @Autowired
    public CertificateRepositoryImplTest(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Test
    public void findById_WhenFound_ShouldReturnOptionalOfCertificate() {
        //given
        //when
        Optional<Certificate> actual = certificateRepository.findById(1L);
        //then
        Optional<Certificate> expected = Optional.of(FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findById_WhenCertificateNotFound_ShouldReturnOptionalEmpty() {
        //given
        //when
        Optional<Certificate> actual = certificateRepository.findById(10000L);
        //then
        Optional<Certificate> expected = Optional.empty();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_WhenNoRequestConditions_ShouldReturnListOfAllCertificates() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(null, null);
        //when
        List<Certificate> results = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        int size = results.size();
        Assertions.assertEquals(5, size);
    }

    @Test
    public void findAll_WhenSearchByNameOrDescriptionSpecified_ShouldReturnListOfSelection() {
        //given
        FilterRequest filterRequest = new FilterRequest("number 1", null);
        SortRequest sortRequest = new SortRequest(Sort.asc("name"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_WhenSearchByNameOrDescriptionMatchesNothing_ShouldReturnEmptyList() {
        //given
        FilterRequest filterRequest = new FilterRequest("_Should found nothing", null);
        SortRequest sortRequest = new SortRequest(null, null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Collections.emptyList();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_WhenSearchByTagNameSpecified_ShouldReturnListOfSelection() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, "tag2");
        SortRequest sortRequest = new SortRequest(Sort.asc("name"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_WhenSearchByTagNameMatchesNothing_ShouldEmptyList() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, "such tag does not exists");
        SortRequest sortRequest = new SortRequest(null, null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Collections.emptyList();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_WhenConjunctionSearchSpecified_ShouldReturnListOfSelection() {
        //given
        FilterRequest filterRequest = new FilterRequest("number 2", "tag1");
        SortRequest sortRequest = new SortRequest(Sort.asc("name"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Collections.singletonList(FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByNameAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("name"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByNameDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("name"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, FOURTH, THIRD, SECOND, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByDescriptionAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("description"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, FIRST, SECOND, THIRD, FOURTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByDescriptionDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("description"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FOURTH, THIRD, SECOND, FIRST, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByPriceAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("price"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FOURTH, FIFTH, FIRST, SECOND, THIRD);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByPriceDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("price"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(THIRD, SECOND, FIRST, FIFTH, FOURTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByCreateDateAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("createDate"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, THIRD, FOURTH, FIFTH, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByCreateDateDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("createDate"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, FIFTH, FOURTH, THIRD, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByLastUpdateDateAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("lastUpdateDate"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOrderedByLastUpdateDateDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("lastUpdateDate"), null);
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, FOURTH, THIRD, SECOND, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByNameAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("name"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, THIRD, FOURTH, FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByNameDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("name"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST, FOURTH, THIRD, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByDescriptionAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("description"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, THIRD, FOURTH, FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByDescriptionDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("description"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST, FOURTH, THIRD, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByPriceAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("price"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, FOURTH, THIRD, FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByPriceDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("price"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST, THIRD, FOURTH, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByCreateDateAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("createDate"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, THIRD, FOURTH, SECOND, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByCreateDateDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("createDate"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, SECOND, FOURTH, THIRD, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByLastUpdateDateAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("lastUpdateDate"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, THIRD, FOURTH, FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListThenOrderedByLastUpdateDateDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("lastUpdateDate"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST, FOURTH, THIRD, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_WhenAllOptionsSpecified_ShouldReturnListOfSelection() {
        //given
        FilterRequest filterRequest = new FilterRequest("number", "tag2");
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("lastUpdateDate"));
        //when
        List<Certificate> actual = certificateRepository.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void create_ShouldReturnCreatedWithId() {
        //given
        //when
        Certificate created = certificateRepository.save(CERTIFICATE_TO_CREATE);
        //then
        Long actual = created.getId();
        Assertions.assertNotNull(actual);
    }

    @Test
    public void create_ShouldSaveCertificateInDatabase() {
        //given
        Certificate created = certificateRepository.save(CERTIFICATE_TO_CREATE);
        Long id = created.getId();
        //when
        Optional<Certificate> found = certificateRepository.findById(id);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE_TO_CREATE)
                .setId(id)
                .build();
        Certificate actual = found.get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void update_ShouldUpdateCertificateInDatabase() {
        //given
        certificateRepository.save(FIFTH_TO_UPDATE);
        //when
        Optional<Certificate> found = certificateRepository.findById(5L);
        //then
        Certificate actual = found.get();
        Assertions.assertEquals(FIFTH_TO_UPDATE, actual);
    }

    @Test
    public void update_ShouldReturnUpdated() {
        //given
        //when
        Certificate actual = certificateRepository.save(FIFTH_TO_UPDATE);
        //then
        Assertions.assertEquals(FIFTH_TO_UPDATE, actual);
    }

    @Test
    public void delete_ShouldDeleteCertificateFromDatabase() {
        //given
        //when
        certificateRepository.delete(FIFTH);
        Optional<Certificate> found = certificateRepository.findById(5L);
        //then
        boolean actual = found.isPresent();
        Assertions.assertFalse(actual);
    }

}
