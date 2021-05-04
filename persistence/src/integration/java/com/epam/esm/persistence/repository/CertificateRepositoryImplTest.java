package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.config.TestPersistenceConfig;
import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.specification.FindAllSpecification;
import com.epam.esm.persistence.specification.certificare.FindNotDeletedByIdSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
            new HashSet<>(TAGS_FIRST),
            false
    );
    private static final Certificate SECOND = new Certificate(
            2L,
            "certificate number 2",
            "description number 3",
            new BigDecimal("44.44"),
            5,
            LocalDateTime.parse("2020-01-01T01:11:11"),
            LocalDateTime.parse("2021-02-02T02:22:22"),
            new HashSet<>(TAGS_SECOND),
            false
    );
    private static final Certificate THIRD = new Certificate(
            3L,
            "certificate number 3",
            "description number 4",
            new BigDecimal("55.55"),
            10,
            LocalDateTime.parse("2020-02-02T02:22:22"),
            LocalDateTime.parse("2021-03-03T03:33:33"),
            Collections.emptySet(),
            false
    );
    private static final Certificate FOURTH = new Certificate(
            4L,
            "certificate number 4",
            "description number 5",
            new BigDecimal("11.11"),
            10,
            LocalDateTime.parse("2020-03-03T03:33:33"),
            LocalDateTime.parse("2021-04-04T04:44:44"),
            Collections.emptySet(),
            false
    );
    private static final Certificate FIFTH = new Certificate(
            5L,
            "certificate number 5",
            "description number 1",
            new BigDecimal("22.22"),
            15,
            LocalDateTime.parse("2020-04-04T04:44:44"),
            LocalDateTime.parse("2021-05-05T05:55:55"),
            Collections.emptySet(),
            false
    );
    private static final Certificate FIFTH_TO_UPDATE = new Certificate(
            5L,
            "certificate name updated",
            "certificate description updated",
            new BigDecimal("42.00"),
            42,
            LOCAL_DATE_TIME_TEST,
            LOCAL_DATE_TIME_TEST,
            Collections.emptySet(),
            false
    );
    private static final Certificate CERTIFICATE_TO_CREATE = new Certificate(
            null,
            "certificate name",
            "certificate description",
            new BigDecimal("42.00"),
            42,
            LOCAL_DATE_TIME_TEST,
            LOCAL_DATE_TIME_TEST,
            Collections.emptySet(),
            false
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
    public void findSingle_ShouldFindBySpecification() {
        //given
        Specification<Certificate> specification = new FindNotDeletedByIdSpecification(2L);
        //when
        Optional<Certificate> actual = certificateRepository.findSingle(specification);
        //then
        Optional<Certificate> expected = Optional.of(SECOND);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void find_ShouldFindPageBySpecification() {
        //given
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        Pageable pageable = PageRequest.of(0, 4, sort);
        Specification<Certificate> specification = new FindAllSpecification<>();
        //when
        Page<Certificate> actual = certificateRepository.find(pageable, specification);
        //then
        Page<Certificate> expected = new PageImpl<>(Arrays.asList(THIRD, SECOND, FIRST, FIFTH), pageable, 5);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void save_ShouldReturnCreatedWithId() {
        //given
        //when
        Certificate created = certificateRepository.save(CERTIFICATE_TO_CREATE);
        //then
        Long actual = created.getId();
        Assertions.assertNotNull(actual);
    }

    @Test
    public void save_ShouldSaveCertificateInDatabase() {
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
    public void save_ShouldUpdateCertificateInDatabase() {
        //given
        certificateRepository.save(FIFTH_TO_UPDATE);
        //when
        Optional<Certificate> found = certificateRepository.findById(5L);
        //then
        Certificate actual = found.get();
        Assertions.assertEquals(FIFTH_TO_UPDATE, actual);
    }

    @Test
    public void save_ShouldReturnUpdated() {
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
        certificateRepository.delete(5L);
        Optional<Certificate> found = certificateRepository.findById(5L);
        //then
        boolean actual = found.isPresent();
        Assertions.assertFalse(actual);
    }

}
