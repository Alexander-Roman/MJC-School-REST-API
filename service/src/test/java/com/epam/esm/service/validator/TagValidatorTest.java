package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class TagValidatorTest {

    private static final int MAX_TAG_NAME_LENGTH = 50;

    private final TagValidator validator = new TagValidator();

    private static Stream<Tag> provideValidTags() {
        return Stream.of(
                new Tag(1L, "name", null),
                new Tag(null, "name", null)
        );
    }

    private static Stream<Tag> provideInvalidTags() {
        String nameLengthExceeded = buildStringExceedingLength(MAX_TAG_NAME_LENGTH);
        return Stream.of(
                null,
                new Tag(0L, "name", null),
                new Tag(-1L, "name", null),
                new Tag(1L, null, null),
                new Tag(1L, "", null),
                new Tag(1L, "  ", null),
                new Tag(1L, " \n ", null),
                new Tag(1L, nameLengthExceeded, null)
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
    @MethodSource("provideValidTags")
    public void isValid_WhenTagValid_ShouldReturnTrue(Tag valid) {
        //given
        //when
        boolean actual = validator.isValid(valid);
        //then
        Assertions.assertTrue(actual);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTags")
    public void isValid_WhenTagInvalid_ShouldReturnFalse(Tag invalid) {
        //given
        //when
        boolean actual = validator.isValid(invalid);
        //then
        Assertions.assertFalse(actual);
    }

}
