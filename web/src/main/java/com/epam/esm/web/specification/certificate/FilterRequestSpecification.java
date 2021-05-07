package com.epam.esm.web.specification.certificate;

/**
 * All specifications that this interface extend are combined into one using AND operation
 */
public interface FilterRequestSpecification
        extends FindAllNotDeletedSpecification, NameOrDescriptionSearchSpecification, AllOfTagNamesSearchSpecification {

}
