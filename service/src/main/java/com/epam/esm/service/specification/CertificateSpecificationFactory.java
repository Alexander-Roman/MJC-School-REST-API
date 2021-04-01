package com.epam.esm.service.specification;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.specification.Specification;
import com.epam.esm.persistence.specification.certificate.AllSpecification;
import com.epam.esm.persistence.specification.certificate.DualSpecification;
import com.epam.esm.persistence.specification.certificate.SearchSpecification;
import com.epam.esm.persistence.specification.certificate.TagNameSpecification;
import com.epam.esm.service.model.FilterRequest;

import java.util.ArrayList;
import java.util.List;


public class CertificateSpecificationFactory {

    private static final Specification<Certificate> DEFAULT_SPECIFICATION = new AllSpecification();

    public static Specification<Certificate> getByFilterRequest(FilterRequest filterRequest) {
        List<Specification<Certificate>> specifications = new ArrayList<>();

        String search = filterRequest.getSearch();
        if (search != null) {
            Specification<Certificate> specification = new SearchSpecification(search);
            specifications.add(specification);
        }

        String tagName = filterRequest.getTagName();
        if (tagName != null) {
            Specification<Certificate> specification = new TagNameSpecification(tagName);
            specifications.add(specification);
        }

        Specification<Certificate> specification = specifications
                .stream()
                .findFirst()
                .orElse(DEFAULT_SPECIFICATION);
        for (int i = 1; i < specifications.size(); i++) {
            Specification<Certificate> additionalSpecification = specifications.get(i);
            specification = new DualSpecification(specification, additionalSpecification);
        }
        return specification;
    }

}
