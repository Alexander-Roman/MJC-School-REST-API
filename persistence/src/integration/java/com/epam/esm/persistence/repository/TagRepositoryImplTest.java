package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.config.TestPersistenceConfig;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.specification.FindAllSpecification;
import com.epam.esm.persistence.specification.tag.FindByNameSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest(classes = {TestPersistenceConfig.class})
@ActiveProfiles("integrationTest")
@Transactional
public class TagRepositoryImplTest {

    private static final Tag TAG_FIRST = new Tag(1L, "tag1", null);
    private static final Tag TAG_TO_CREATE = new Tag(null, "tag6", null);

    private final TagRepository tagRepository;

    @Autowired
    public TagRepositoryImplTest(TagRepository tagRepository) {
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
        Specification<Tag> specification = new FindByNameSpecification("tag1");
        //when
        Optional<Tag> actual = tagRepository.findOne(specification);
        //then
        Optional<Tag> expected = Optional.of(TAG_FIRST);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findByName_WhenTagNotFound_ShouldReturnOptionalEmpty() {
        //given
        Specification<Tag> specification = new FindByNameSpecification("such name does not exists");
        //when
        Optional<Tag> actual = tagRepository.findOne(specification);
        //then
        Optional<Tag> expected = Optional.empty();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findAll_ShouldReturnPageOfAllTags() {
        //given
        Pageable pageable = PageRequest.of(0, 20);
        Specification<Tag> specification = new FindAllSpecification<>();
        //when
        Page<Tag> results = tagRepository.findAll(specification, pageable);
        //then
        int size = results.getNumberOfElements();
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
        tagRepository.deleteById(1L);
        Optional<Tag> found = tagRepository.findById(1L);
        //then
        boolean actual = found.isPresent();
        Assertions.assertFalse(actual);
    }

}
