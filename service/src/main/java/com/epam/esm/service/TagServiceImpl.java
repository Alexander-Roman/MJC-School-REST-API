package com.epam.esm.service;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private static final String ERROR_MESSAGE_ID_INVALID = "Invalid ID parameter: ";
    private static final String ERROR_MESSAGE_TAG_NOT_FOUND = "Tag does not exists! ID: ";
    private static final String ERROR_MESSAGE_TAG_INVALID = "Tag invalid: ";
    private static final long MIN_ID_VALUE = 1L;

    private final TagDao tagDao;
    private final CertificateTagService certificateTagService;
    private final Validator<Tag> tagValidator;
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagDao tagDao,
                          CertificateTagService certificateTagService,
                          Validator<Tag> tagValidator,
                          TagRepository tagRepository) {
        this.tagDao = tagDao;
        this.certificateTagService = certificateTagService;
        this.tagValidator = tagValidator;
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag findById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        return tagRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_TAG_NOT_FOUND + id));
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new ServiceException(ERROR_MESSAGE_TAG_INVALID + tag);
        }
        String name = tag.getName();
        Optional<Tag> found = tagRepository.findByName(name);
        return found.orElseGet(() -> tagRepository.save(tag));
    }

    @Override
    @Transactional
    public Set<Tag> createIfNotExist(Set<Tag> tags) {
        Preconditions.checkNotNull(tags, "Invalid tags parameter: " + tags);

        for (Tag tag : tags) {
            if (!tagValidator.isValid(tag)) {
                throw new ServiceException(ERROR_MESSAGE_TAG_INVALID + tag);
            }
        }

        Set<Tag> results = new HashSet<>();
        for (Tag tag : tags) {
            String name = tag.getName();
            Optional<Tag> result = tagRepository.findByName(name);
            if (result.isPresent()) {
                Tag found = result.get();
                results.add(found);
            } else {
                Tag created = tagRepository.save(tag);
                results.add(created);
            }
        }
        return results;
    }

    @Override
    @Transactional
    public Tag deleteById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        Tag target = tagRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_TAG_NOT_FOUND + id));
        tagRepository.delete(target);
        return target;
    }

    @Override
    public void deleteUnused() {
        tagRepository.deleteUnused();
    }

}
