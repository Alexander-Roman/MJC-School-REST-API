package com.epam.esm.service.validator;

import com.epam.esm.persistence.model.Sort;
import com.epam.esm.persistence.model.SortRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CertificateCertificateSortRequestValidatorImplTest {

    private final CertificateCertificateSortRequestValidatorImpl validator = new CertificateCertificateSortRequestValidatorImpl();

    private static Stream<SortRequest> provideValidSortRequests() {
        return Stream.of(
                new SortRequest(null, null),
                new SortRequest(Sort.asc("name"), null),
                new SortRequest(Sort.asc("description"), null),
                new SortRequest(Sort.asc("price"), null),
                new SortRequest(Sort.asc("duration"), null),
                new SortRequest(Sort.asc("createDate"), null),
                new SortRequest(Sort.asc("lastUpdateDate"), null),
                new SortRequest(Sort.desc("name"), Sort.asc("name")),
                new SortRequest(Sort.desc("description"), Sort.asc("description")),
                new SortRequest(Sort.desc("price"), Sort.asc("price")),
                new SortRequest(Sort.desc("duration"), Sort.asc("duration")),
                new SortRequest(Sort.desc("createDate"), Sort.asc("createDate")),
                new SortRequest(Sort.desc("lastUpdateDate"), Sort.asc("lastUpdateDate"))
        );
    }

    private static Stream<SortRequest> provideInvalidSortRequests() {
        return Stream.of(
                null,
                new SortRequest(new Sort("name", null), null),
                new SortRequest(new Sort("description", null), null),
                new SortRequest(new Sort("price", null), null),
                new SortRequest(new Sort("duration", null), null),
                new SortRequest(new Sort("createDate", null), null),
                new SortRequest(new Sort("lastUpdateDate", null), null),
                new SortRequest(null, new Sort("name", null)),
                new SortRequest(null, new Sort("description", null)),
                new SortRequest(null, new Sort("price", null)),
                new SortRequest(null, new Sort("duration", null)),
                new SortRequest(null, new Sort("createDate", null)),
                new SortRequest(null, new Sort("lastUpdateDate", null)),
                new SortRequest(Sort.asc("id"), null),
                new SortRequest(Sort.asc("tags"), null),
                new SortRequest(Sort.asc("name"), Sort.desc("id")),
                new SortRequest(Sort.asc("description"), Sort.desc("tags"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidSortRequests")
    public void testIsValidShouldReturnTrueWhenSortRequestIsValidForCertificate(SortRequest valid) {
        //given
        //when
        boolean actual = validator.isValid(valid);
        //then
        Assertions.assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidSortRequests")
    public void testIsValidShouldReturnFalseWhenSortRequestInvalidForCertificate(SortRequest invalid) {
        //given
        //when
        boolean actual = validator.isValid(invalid);
        //then
        Assertions.assertFalse(actual);
    }

}
