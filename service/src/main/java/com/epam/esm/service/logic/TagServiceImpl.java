package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.TagRepository;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.validator.Validator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final Validator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,
                          Validator<Tag> tagValidator) {
        this.tagRepository = tagRepository;
        this.tagValidator = tagValidator;
    }

    @Override
    public List<Long> createIfNotExist(Set<Tag> tags) {
        Preconditions.checkArgument(tags != null && !tags.isEmpty(), "Invalid tags parameter: " + tags);
        for (Tag tag : tags) {
            if (!tagValidator.isValid(tag)) {
                throw new ServiceException("Invalid tag in collection: " + tag);
            }
//            if (tag.getId() != null) {
//                throw new ServiceException("Specifying ids is now allowed for new tags! Tag invalid: " + tag);
//            }
        }
        return tagRepository.saveIfNotExist(tags);
    }

}
