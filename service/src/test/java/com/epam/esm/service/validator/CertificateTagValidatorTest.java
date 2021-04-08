package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.CertificateTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CertificateTagValidatorTest {

    private final CertificateTagValidator validator = new CertificateTagValidator();

    private static Stream<CertificateTag> provideValidCertificateTags() {
        return Stream.of(
                new CertificateTag(null, 1L, 1L),
                new CertificateTag(1L, 1L, 1L)
        );
    }

    private static Stream<CertificateTag> provideInvalidCertificateTags() {
        return Stream.of(
                null,
                new CertificateTag(0L, 1L, 1L),
                new CertificateTag(-1L, 1L, 1L),
                new CertificateTag(1L, null, 1L),
                new CertificateTag(1L, 0L, 1L),
                new CertificateTag(1L, -1L, 1L),
                new CertificateTag(1L, 1L, null),
                new CertificateTag(1L, 1L, 0L),
                new CertificateTag(1L, 1L, -1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidCertificateTags")
    public void testIsValidShouldReturnTrueWhenCertificateTagIsValid(CertificateTag valid) {
        //given
        //when
        boolean actual = validator.isValid(valid);
        //then
        Assertions.assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCertificateTags")
    public void testIsValidShouldReturnFalseWhenCertificateTagInvalid(CertificateTag invalid) {
        //given
        //when
        boolean actual = validator.isValid(invalid);
        //then
        Assertions.assertFalse(actual);
    }

}
