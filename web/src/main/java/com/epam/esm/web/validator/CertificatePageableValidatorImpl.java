package com.epam.esm.web.validator;

import com.epam.esm.persistence.entity.Certificate_;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CertificatePageableValidatorImpl implements CertificatePageableValidator {

    @Override
    public void validate(@NonNull Object object, @NonNull Errors errors) {
        Pageable pageable = (Pageable) object;

        Sort sort = pageable.getSort();

        sort.get()
                .forEach(order -> this.validate(order, errors));
    }

    private void validate(Sort.Order order, Errors errors) {
        String property = order.getProperty();
        if (!this.isValid(property)) {
            errors.rejectValue("sort", "pageable.sort.order.property.invalid", "Sort property invalid!");
        }
    }

    private boolean isValid(String property) {
        return Certificate_.NAME.equals(property)
                || Certificate_.DESCRIPTION.equals(property)
                || Certificate_.PRICE.equals(property)
                || Certificate_.DURATION.equals(property)
                || Certificate_.CREATE_DATE.equals(property)
                || Certificate_.LAST_UPDATE_DATE.equals(property);
    }

}
