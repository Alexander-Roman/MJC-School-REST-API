package com.epam.esm.service;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.specification.tag.FindByNameSpecification;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private static final String ERROR_MESSAGE_ID_INVALID = "Invalid ID parameter: ";
    private static final String ERROR_MESSAGE_TAG_NOT_FOUND = "Tag does not exists! ID: ";
    private static final String ERROR_MESSAGE_TAG_INVALID = "Tag invalid: ";
    private static final long MIN_ID_VALUE = 1L;

    private final TagRepository tagRepository;
    private final Validator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          Validator<Tag> tagValidator) {
        this.tagRepository = tagRepository;
        this.tagValidator = tagValidator;
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
    public Page<Tag> find(Pageable pageable, Specification<Tag> specification) {
        Preconditions.checkNotNull(pageable, "Pageable argument invalid: " + pageable);
        Preconditions.checkNotNull(specification, "Specification argument invalid: " + specification);

        return tagRepository.findAll(specification, pageable);
    }

    @Override
    public Tag create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new ServiceException(ERROR_MESSAGE_TAG_INVALID + tag);
        }
        String name = tag.getName();
        Specification<Tag> specification = new FindByNameSpecification(name);
        Optional<Tag> found = tagRepository.findOne(specification);
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
            Specification<Tag> specification = new FindByNameSpecification(name);
            Optional<Tag> result = tagRepository.findOne(specification);
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
        tagRepository.deleteById(id);
        return target;
    }

    @Override
    public Tag findMostPurchasedByTopAccount() {
        Optional<Tag> found = tagRepository.findMostPurchasedByTopAccount();
        return found
                .orElseThrow(() -> new EntityNotFoundException("No tag found matching the conditions!"));
    }
}
