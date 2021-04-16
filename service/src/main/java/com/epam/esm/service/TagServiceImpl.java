package com.epam.esm.service;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.EntityAlreadyExistsException;
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

    @Autowired
    public TagServiceImpl(TagDao tagDao, CertificateTagService certificateTagService, Validator<Tag> tagValidator) {
        this.tagDao = tagDao;
        this.certificateTagService = certificateTagService;
        this.tagValidator = tagValidator;
    }

    @Override
    public Tag findById(Long id) {
        Preconditions.checkNotNull(id, ERROR_MESSAGE_ID_INVALID + id);
        Preconditions.checkArgument(id >= MIN_ID_VALUE, ERROR_MESSAGE_ID_INVALID + id);

        return tagDao
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_TAG_NOT_FOUND + id));
    }

    @Override
    public List<Tag> findAll() {
        return tagDao.findAll();
    }

    @Override
    public Tag create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new ServiceException(ERROR_MESSAGE_TAG_INVALID + tag);
        }
        String name = tag.getName();
        Optional<Tag> found = tagDao.findByName(name);
        return found.orElseGet(() -> tagDao.create(tag));
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
            Optional<Tag> result = tagDao.findByName(name);
            if (result.isPresent()) {
                Tag found = result.get();
                results.add(found);
            } else {
                Tag created = tagDao.create(tag);
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

        Tag target = tagDao
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_TAG_NOT_FOUND + id));

        certificateTagService.deleteByTagId(id);
        tagDao.delete(id);

        return target;
    }

    @Override
    public void deleteUnused() {
        tagDao.deleteUnused();
    }

}
