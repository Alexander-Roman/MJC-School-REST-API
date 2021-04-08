package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CertificateValidatorTest {

    private static final int MAX_NAME_LENGTH = 150;
    private static final int MAX_DESCRIPTION_LENGTH = 255;

    private final Validator<Tag> tagValidator = Mockito.mock(TagValidator.class);
    private final CertificateValidator certificateValidator = new CertificateValidator(tagValidator);

    private static Stream<Certificate> provideValidCertificates() {
        return Stream.of(
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(null, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet())
        );
    }

    private static Stream<Certificate> provideInvalidCertificates() {
        String nameLengthExceeded = buildStringExceedingLength(MAX_NAME_LENGTH);
        String descriptionLengthExceeded = buildStringExceedingLength(MAX_DESCRIPTION_LENGTH);
        return Stream.of(
                null,
                new Certificate(0L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(-1L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, null, "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "  ", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, " \n ", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, nameLengthExceeded, "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", null, new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "  ", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", " \n ", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", descriptionLengthExceeded, new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "description", null, 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "description", new BigDecimal("-1.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "description", new BigDecimal("100000.00"), 1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), 0, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), -1, LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), 1, null, LocalDateTime.now(), Collections.emptySet()),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), null, Collections.emptySet()),
                new Certificate(1L, "name", "description", new BigDecimal("10.00"), 1, LocalDateTime.now(), LocalDateTime.now(), null)
        );
    }

    private static String buildStringExceedingLength(int length) {
        StringBuilder stringBuilder = new StringBuilder("The length of the string exceeds " + length + " characters:");
        for (int i = 0; i <= length; i++) {
            stringBuilder.append(".");
        }
        return stringBuilder.toString();
    }

    @ParameterizedTest
    @MethodSource("provideValidCertificates")
    public void testIsValidShouldReturnTrueWhenCertificateValid(Certificate valid) {
        //given
        //when
        boolean actual = certificateValidator.isValid(valid);
        //then
        Assertions.assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCertificates")
    public void testIsValidShouldReturnFalseWhenCertificateInvalid(Certificate invalid) {
        //given
        //when
        boolean actual = certificateValidator.isValid(invalid);
        //then
        Assertions.assertFalse(actual);
    }

    @Test
    public void testIsValidShouldReturnFalseWhenOneOfTagsInvalid() {
        //given
        Tag tag = new Tag(1L, null);
        Set<Tag> tags = new HashSet<>(Collections.singletonList(tag));
        Certificate validExceptTags = new Certificate(
                1L,
                "name",
                "description",
                new BigDecimal("42.00"),
                13,
                LocalDateTime.now(),
                LocalDateTime.now(),
                tags
        );
        when(tagValidator.isValid(any())).thenReturn(false);
        //when
        boolean actual = certificateValidator.isValid(validExceptTags);
        //then
        Assertions.assertFalse(actual);
    }

}
