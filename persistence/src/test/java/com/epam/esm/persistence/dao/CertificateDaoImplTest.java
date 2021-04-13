package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.config.TestPersistenceConfig;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.model.FilterRequest;
import com.epam.esm.persistence.model.Sort;
import com.epam.esm.persistence.model.SortRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@ActiveProfiles("test")
@Transactional
public class CertificateDaoImplTest {

    private static final LocalDateTime LOCAL_DATE_TIME_TEST = LocalDateTime.parse("2021-01-01T12:00:00");
    private static final List<Tag> TAGS_FIRST = Arrays.asList(
            new Tag(1L, "tag1"),
            new Tag(2L, "tag2")
    );
    private static final List<Tag> TAGS_SECOND = Collections.singletonList(
            new Tag(2L, "tag2")
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

    private final CertificateDaoImpl certificateDao;

    @Autowired
    public CertificateDaoImplTest(CertificateDaoImpl certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Test
    public void testFindByIdShouldReturnOptionalOfCertificateWhenFound() {
        //given
        //when
        Optional<Certificate> actual = certificateDao.findById(1L);
        //then
        Optional<Certificate> expected = Optional.of(FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByIdShouldReturnOptionalEmptyWhenCertificateNotFound() {
        //given
        //when
        Optional<Certificate> actual = certificateDao.findById(10000L);
        //then
        Optional<Certificate> expected = Optional.empty();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOfAllCertificates() {
        //given
        //when
        List<Certificate> results = certificateDao.findAll();
        //then
        int size = results.size();
        Assertions.assertEquals(5, size);
    }

    @Test
    public void testFindAllShouldReturnListOfAllCertificatesWhenNoRequestConditions() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(null, null);
        //when
        List<Certificate> results = certificateDao.findAll(sortRequest, filterRequest);
        //then
        int size = results.size();
        Assertions.assertEquals(5, size);
    }

    @Test
    public void testFindAllShouldReturnListOfSelectionWhenSearchByNameOrDescriptionSpecified() {
        //given
        FilterRequest filterRequest = new FilterRequest("number 1", null);
        SortRequest sortRequest = new SortRequest(Sort.asc("name"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnEmptyListWhenSearchByNameOrDescriptionMatchesNothing() {
        //given
        FilterRequest filterRequest = new FilterRequest("Should found nothing", null);
        SortRequest sortRequest = new SortRequest(null, null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Collections.emptyList();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOfSelectionWhenSearchByTagNameSpecified() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, "tag2");
        SortRequest sortRequest = new SortRequest(Sort.asc("name"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldEmptyListWhenSearchByTagNameMatchesNothing() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, "such tag does not exists");
        SortRequest sortRequest = new SortRequest(null, null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Collections.emptyList();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOfSelectionWhenConjunctionSearchSpecified() {
        //given
        FilterRequest filterRequest = new FilterRequest("number 2", "tag1");
        SortRequest sortRequest = new SortRequest(Sort.asc("name"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Collections.singletonList(FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByNameAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("name"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByNameDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("name"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, FOURTH, THIRD, SECOND, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByDescriptionAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("description"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, FIRST, SECOND, THIRD, FOURTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByDescriptionDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("description"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FOURTH, THIRD, SECOND, FIRST, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByPriceAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("price"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FOURTH, FIFTH, FIRST, SECOND, THIRD);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByPriceDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("price"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(THIRD, SECOND, FIRST, FIFTH, FOURTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByCreateDateAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("createDate"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, THIRD, FOURTH, FIFTH, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByCreateDateDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("createDate"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, FIFTH, FOURTH, THIRD, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByLastUpdateDateAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("lastUpdateDate"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, SECOND, THIRD, FOURTH, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOrderedByLastUpdateDateDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("lastUpdateDate"), null);
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, FOURTH, THIRD, SECOND, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByNameAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("name"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, THIRD, FOURTH, FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByNameDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("name"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST, FOURTH, THIRD, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByDescriptionAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("description"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, THIRD, FOURTH, FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByDescriptionDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("description"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST, FOURTH, THIRD, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByPriceAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("price"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, FOURTH, THIRD, FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByPriceDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("price"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST, THIRD, FOURTH, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByCreateDateAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("createDate"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, THIRD, FOURTH, SECOND, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByCreateDateDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("createDate"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIRST, SECOND, FOURTH, THIRD, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByLastUpdateDateAsc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.desc("duration"), Sort.asc("lastUpdateDate"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(FIFTH, THIRD, FOURTH, FIRST, SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListThenOrderedByLastUpdateDateDesc() {
        //given
        FilterRequest filterRequest = new FilterRequest(null, null);
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("lastUpdateDate"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST, FOURTH, THIRD, FIFTH);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOfSelectionWhenAllOptionsSpecified() {
        //given
        FilterRequest filterRequest = new FilterRequest("number", "tag2");
        SortRequest sortRequest = new SortRequest(Sort.asc("duration"), Sort.desc("lastUpdateDate"));
        //when
        List<Certificate> actual = certificateDao.findAll(sortRequest, filterRequest);
        //then
        List<Certificate> expected = Arrays.asList(SECOND, FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCreateShouldReturnCreatedWithId() {
        //given
        //when
        Certificate created = certificateDao.create(CERTIFICATE_TO_CREATE);
        //then
        Long actual = created.getId();
        Assertions.assertNotNull(actual);
    }

    @Test
    public void testCreateShouldSaveCertificateInDatabase() {
        //given
        Certificate created = certificateDao.create(CERTIFICATE_TO_CREATE);
        Long id = created.getId();
        //when
        Optional<Certificate> found = certificateDao.findById(id);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE_TO_CREATE)
                .setId(id)
                .build();
        Certificate actual = found.get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testUpdateShouldUpdateCertificateInDatabase() {
        //given
        certificateDao.update(FIFTH_TO_UPDATE);
        //when
        Optional<Certificate> found = certificateDao.findById(5L);
        //then
        Certificate actual = found.get();
        Assertions.assertEquals(FIFTH_TO_UPDATE, actual);
    }

    @Test
    public void testUpdateShouldReturnUpdated() {
        //given
        //when
        Certificate actual = certificateDao.update(FIFTH_TO_UPDATE);
        //then
        Assertions.assertEquals(FIFTH_TO_UPDATE, actual);
    }

    @Test
    public void testDeleteShouldDeleteCertificateFromDatabase() {
        //given
        //when
        certificateDao.delete(5L);
        Optional<Certificate> found = certificateDao.findById(5L);
        //then
        boolean actual = found.isPresent();
        Assertions.assertFalse(actual);
    }

}
