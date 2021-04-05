package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.CertificateTagDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.repository.CertificateTagRepository;
import com.epam.esm.persistence.repository.TagRepository;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.specification.Specification;
import com.epam.esm.persistence.specification.tag.AllSpecification;
import com.epam.esm.persistence.specification.tag.IdSpecification;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    public static final long MIN_ID_VALUE = 1L;

    private final TagRepository tagRepository;
    private final CertificateTagRepository certificateTagRepository;
    private final Validator<Tag> tagValidator;
    private final TagDao tagDao;
    private final CertificateTagDao certificateTagDao;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          CertificateTagRepository certificateTagRepository,
                          Validator<Tag> tagValidator,
                          TagDao tagDao,
                          CertificateTagDao certificateTagDao) {
        this.tagRepository = tagRepository;
        this.certificateTagRepository = certificateTagRepository;
        this.tagValidator = tagValidator;
        this.tagDao = tagDao;
        this.certificateTagDao = certificateTagDao;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Preconditions.checkArgument(id != null && id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);

//        Specification<Tag> specification = new IdSpecification(id);
//        List<Tag> results = tagRepository.find(specification);
//        return results
//                .stream()
//                .findFirst();
        return tagDao.findById(id);
    }

    @Override
    public List<Tag> findAll() {
//        Specification<Tag> specification = new AllSpecification();
//        return tagRepository.find(specification);
        return tagDao.findAll();
    }

    @Override
    public Tag create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new ServiceException("Tag invalid: " + tag);
        }
        if (tag.getId() != null) {
            throw new ServiceException("Specifying id is now allowed for new tag! Tag invalid: " + tag);
        }
//        return tagRepository.create(tag);
        return tagDao.create(tag);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Preconditions.checkArgument(id != null && id >= MIN_ID_VALUE, "Invalid ID parameter: " + id);
//        certificateTagRepository.deleteByTagId(id);
        certificateTagDao.deleteByTagId(id);
//        tagRepository.delete(id);
        tagDao.delete(id);
    }

    @Override
    public void createIfNotExist(Set<Tag> tags) {
        Preconditions.checkArgument(tags != null && !tags.isEmpty(), "Invalid tags parameter: " + tags);
        for (Tag tag : tags) {
            if (!tagValidator.isValid(tag)) {
                throw new ServiceException("Invalid tag in collection: " + tag);
            }
        }
//        return tagRepository.createIfNotExists(tags);
        tagDao.createIfNotExists(tags);
    }

    @Override
    public void deleteUnused() {
//        tagRepository.deleteUnused();
        tagDao.deleteUnused();
    }

}
