package com.epam.esm.web.validator.constraint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PageableValidator implements ConstraintValidator<AllowedOrderProperties, Pageable> {

    private Set<String> allowedOrderProperties;

    @Override
    public void initialize(AllowedOrderProperties constraintAnnotation) {
        String[] values = constraintAnnotation.value();
        List<String> propertiesList = Arrays.asList(values);
        allowedOrderProperties = new HashSet<>(propertiesList);
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        if (pageable == null) {
            return true;
        }
        if (allowedOrderProperties.isEmpty()) {
            return true;
        }
        Sort sort = pageable.getSort();
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            if (!allowedOrderProperties.contains(property)) {
                return false;
            }
        }
        return true;
    }

}
