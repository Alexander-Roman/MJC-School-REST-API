package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.config.TestPersistenceConfig;
import com.epam.esm.persistence.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@ActiveProfiles("integrationTest")
@Transactional
public class TagDaoImplTest {

    private static final Tag TAG_FIRST = new Tag(1L, "tag1");
    private static final Tag TAG_TO_CREATE = new Tag(null, "tag6");

    private final TagDaoImpl tagDao;

    @Autowired
    public TagDaoImplTest(TagDaoImpl tagDao) {
        this.tagDao = tagDao;
    }

    @Test
    public void testFindByIdShouldReturnOptionalOfTagWhenFound() {
        //given
        //when
        Optional<Tag> actual = tagDao.findById(1L);
        //then
        Optional<Tag> expected = Optional.of(TAG_FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByIdShouldReturnOptionalEmptyWhenTagNotFound() {
        //given
        //when
        Optional<Tag> actual = tagDao.findById(10000L);
        //then
        Optional<Tag> expected = Optional.empty();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByNameShouldReturnOptionalOfTagWhenFound() {
        //given
        //when
        Optional<Tag> actual = tagDao.findByName("tag1");
        //then
        Optional<Tag> expected = Optional.of(TAG_FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByNameShouldReturnOptionalEmptyWhenTagNotFound() {
        //given
        //when
        Optional<Tag> actual = tagDao.findByName("such name does not exists");
        //then
        Optional<Tag> expected = Optional.empty();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindAllShouldReturnListOfAllTags() {
        //given
        //when
        List<Tag> results = tagDao.findAll();
        //then
        int size = results.size();
        Assertions.assertEquals(5, size);
    }

    @Test
    public void testCreateShouldReturnCreatedWithId() {
        //given
        //when
        Tag created = tagDao.create(TAG_TO_CREATE);
        //then
        Long actual = created.getId();
        Assertions.assertNotNull(actual);
    }

    @Test
    public void testCreateShouldSaveTagInDatabase() {
        //given
        Tag created = tagDao.create(TAG_TO_CREATE);
        Long id = created.getId();
        //when
        Optional<Tag> found = tagDao.findById(id);
        //then
        Tag expected = Tag.Builder
                .from(TAG_TO_CREATE)
                .setId(id)
                .build();
        Tag actual = found.get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testDeleteShouldDeleteTagFromDatabase() {
        //given
        //when
        tagDao.delete(5L);
        Optional<Tag> found = tagDao.findById(5L);
        //then
        boolean actual = found.isPresent();
        Assertions.assertFalse(actual);
    }

    @Test
    public void testDeleteUnusedShouldDeleteAllTagNotAttachedToAnyCertificate() {
        //given
        //when
        tagDao.deleteUnused();
        List<Tag> actual = tagDao.findAll();
        //then
        int size = actual.size();
        Assertions.assertEquals(2, size);
    }

}
