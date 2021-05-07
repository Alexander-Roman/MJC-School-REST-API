package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public class CertificateValidatorTest {

    private static final int MAX_NAME_LENGTH = 150;
    private static final int MAX_DESCRIPTION_LENGTH = 255;

    private static final List<Tag> TAGS_LIST = Collections.singletonList(
            new Tag(1L, "name", null)
    );

    private static final Certificate CERTIFICATE_WITH_ID = new Certificate(
            1L,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LocalDateTime.now(),
            LocalDateTime.now(),
            new HashSet<>(TAGS_LIST),
            false
    );
    private static final Certificate CERTIFICATE_WITHOUT_ID = new Certificate(
            null,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LocalDateTime.now(),
            LocalDateTime.now(),
            new HashSet<>(TAGS_LIST),
            false
    );

    private final Validator<Tag> tagValidator = Mockito.mock(TagValidator.class);
    private final CertificateValidator certificateValidator = new CertificateValidator(tagValidator);

    private static Stream<Certificate> provideValidCertificates() {
        return Stream.of(
                CERTIFICATE_WITH_ID,
                CERTIFICATE_WITHOUT_ID
        );
    }

    private static Stream<Certificate> provideInvalidCertificates() {
        String nameLengthExceeded = buildStringExceedingLength(MAX_NAME_LENGTH);
        String descriptionLengthExceeded = buildStringExceedingLength(MAX_DESCRIPTION_LENGTH);
        return Stream.of(
                null,
                new Certificate(0L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(-1L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, null, "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "  ", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, " \n ", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, nameLengthExceeded, "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", null, new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", "", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", "  ", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", " \n ", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", descriptionLengthExceeded, new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", "description", null, 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", "description", new BigDecimal("-1.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", "description", new BigDecimal("100000.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), 0, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), -1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), false),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), null, false),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet(), null)
        );
    }

    private static String buildStringExceedingLength(int length) {
        StringBuilder stringBuilder = new StringBuilder("The length of the string exceeds " + length + " characters:");
        for (int i = 0; i <= length; i++) {
            stringBuilder.append(".");
        }
        return stringBuilder.toString();
    }

    @BeforeEach
    public void setUp() {
        //Positive scenario
        lenient().when(tagValidator.isValid(any())).thenReturn(true);
    }

    @ParameterizedTest
    @MethodSource("provideValidCertificates")
    public void isValid_WhenCertificateValid_ShouldReturnTrue(Certificate valid) {
        //given
        //when
        boolean actual = certificateValidator.isValid(valid);
        //then
        Assertions.assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCertificates")
    public void isValid_WhenCertificateInvalid_ShouldReturnFalse(Certificate invalid) {
        //given
        //when
        boolean actual = certificateValidator.isValid(invalid);
        //then
        Assertions.assertFalse(actual);
    }

    @Test
    public void isValid_WhenOneOfTagsInvalid_ShouldReturnFalse() {
        //given
        lenient().when(tagValidator.isValid(any())).thenReturn(false);
        //when
        boolean actual = certificateValidator.isValid(CERTIFICATE_WITH_ID);
        //then
        Assertions.assertFalse(actual);
    }

}
