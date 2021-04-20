package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.config.TestPersistenceConfig;
import com.epam.esm.persistence.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {TestPersistenceConfig.class})
@ActiveProfiles("integrationTest")
@Transactional
public class TagRepositoryImplTest {

    private static final Tag TAG_FIRST = new Tag(1L, "tag1", null);
    private static final Tag TAG_TO_CREATE = new Tag(null, "tag6", null);

    private final TagRepositoryImpl tagRepository;

    @Autowired
    public TagRepositoryImplTest(TagRepositoryImpl tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Test
    public void findById_WhenFound_ShouldReturnOptionalOfTag() {
        //given
        //when
        Optional<Tag> actual = tagRepository.findById(1L);
        //then
        Optional<Tag> expected = Optional.of(TAG_FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findById_WhenTagNotFound_ShouldReturnOptionalEmpty() {
        //given
        //when
        Optional<Tag> actual = tagRepository.findById(10000L);
        //then
        Optional<Tag> expected = Optional.empty();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByName_WhenFound_ShouldReturnOptionalOfTag() {
        //given
        //when
        Optional<Tag> actual = tagRepository.findByName("tag1");
        //then
        Optional<Tag> expected = Optional.of(TAG_FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByName_WhenTagNotFound_ShouldReturnOptionalEmpty() {
        //given
        //when
        Optional<Tag> actual = tagRepository.findByName("such name does not exists");
        //then
        Optional<Tag> expected = Optional.empty();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnListOfAllTags() {
        //given
        //when
        List<Tag> results = tagRepository.findAll();
        //then
        int size = results.size();
        Assertions.assertEquals(5, size);
    }

    @Test
    public void create_ShouldReturnCreatedWithId() {
        //given
        //when
        Tag created = tagRepository.save(TAG_TO_CREATE);
        //then
        Long actual = created.getId();
        Assertions.assertNotNull(actual);
    }

    @Test
    public void create_ShouldSaveTagInDatabase() {
        //given
        Tag created = tagRepository.save(TAG_TO_CREATE);
        Long id = created.getId();
        //when
        Optional<Tag> found = tagRepository.findById(id);
        //then
        Tag expected = Tag.Builder
                .from(TAG_TO_CREATE)
                .setId(id)
                .build();
        Tag actual = found.get();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void delete_ShouldDeleteTagFromDatabase() {
        //given
        //when
        tagRepository.delete(TAG_FIRST);
        Optional<Tag> found = tagRepository.findById(1L);
        //then
        boolean actual = found.isPresent();
        Assertions.assertFalse(actual);
    }


}
