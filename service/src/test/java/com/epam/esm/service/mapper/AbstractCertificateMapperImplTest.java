package com.epam.esm.service.mapper;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AbstractCertificateMapperImplTest {

    private final AbstractCertificateMapperImpl mapper = new AbstractCertificateMapperImpl();

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

    private static final List<Tag> TAGS = Arrays.asList(
            new Tag(1L, "tag1", null),
            new Tag(2L, "tag2", null),
            new Tag(3L, "tag3", null)
    );

    private static final Certificate CERTIFICATE = new Certificate(
            1L,
            "name",
            "description",
            new BigDecimal("42.00"),
            13,
            LOCAL_DATE_TIME,
            LOCAL_DATE_TIME,
            new HashSet<>(TAGS),
            false
    );

    @Test
    public void merge_ShouldIgnoreId() {
        //given
        Certificate source = new Certificate.Builder()
                .setId(2L)
                .build();
        //when
        Certificate actual = mapper.merge(source, CERTIFICATE);
        //then
        Assertions.assertEquals(CERTIFICATE, actual);
    }

    @Test
    public void merge_ShouldIgnoreCreateDate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Certificate source = new Certificate.Builder()
                .setCreateDate(now)
                .build();
        //when
        Certificate actual = mapper.merge(source, CERTIFICATE);
        //then
        Assertions.assertEquals(CERTIFICATE, actual);
    }

    @Test
    public void merge_ShouldIgnoreLastUpdateDate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Certificate source = new Certificate.Builder()
                .setLastUpdateDate(now)
                .build();
        //when
        Certificate actual = mapper.merge(source, CERTIFICATE);
        //then
        Assertions.assertEquals(CERTIFICATE, actual);
    }


    @Test
    public void merge_WhenOnlyNameSpecified_ShouldUpdateOnlyName() {
        //given
        Certificate updateName = new Certificate.Builder()
                .setId(1L)
                .setName("Updated name")
                .build();
        //when
        Certificate actual = mapper.merge(updateName, CERTIFICATE);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE)
                .setName("Updated name")
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void merge_WhenOnlyDescriptionSpecified_ShouldUpdateOnlyDescription() {
        //given
        Certificate updateDescription = new Certificate.Builder()
                .setId(1L)
                .setDescription("Updated description")
                .build();
        //when
        Certificate actual = mapper.merge(updateDescription, CERTIFICATE);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE)
                .setDescription("Updated description")
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void merge_WhenOnlyPriceSpecified_ShouldUpdateOnlyPrice() {
        //given
        BigDecimal price = new BigDecimal("10.00");
        Certificate updatePrice = new Certificate.Builder()
                .setId(1L)
                .setPrice(price)
                .build();
        //when
        Certificate actual = mapper.merge(updatePrice, CERTIFICATE);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE)
                .setPrice(price)
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void merge_WhenOnlyDurationSpecified_ShouldUpdateOnlyDuration() {
        //given
        Certificate updateDuration = new Certificate.Builder()
                .setId(1L)
                .setDuration(1)
                .build();
        //when
        Certificate actual = mapper.merge(updateDuration, CERTIFICATE);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE)
                .setDuration(1)
                .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void merge_WhenOnlyTagsSpecified_ShouldUpdateOnlyTags() {
        //given
        Set<Tag> tags = Collections.emptySet();
        Certificate updateTags = new Certificate.Builder()
                .setId(1L)
                .setTags(tags)
                .build();
        //when
        Certificate actual = mapper.merge(updateTags, CERTIFICATE);
        //then
        Certificate expected = Certificate.Builder
                .from(CERTIFICATE)
                .setTags(tags)
                .build();
        Assertions.assertEquals(expected, actual);
    }

}
