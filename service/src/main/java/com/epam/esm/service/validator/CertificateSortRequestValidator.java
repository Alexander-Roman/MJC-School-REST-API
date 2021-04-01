package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.Sort;
import com.epam.esm.persistence.model.SortRequest;
import org.springframework.stereotype.Component;

@Component
public class CertificateSortRequestValidator implements SortRequestValidator<Certificate> {

    @Override
    public boolean isValid(SortRequest sortRequest) {
        if (sortRequest == null) {
            return false;
        }
        Sort sort = sortRequest.getSort();
        Sort thenSort = sortRequest.getThenSort();
        return this.isValid(sort) && this.isValid(thenSort);
    }

    private boolean isValid(Sort sort) {
        if (sort == null) {
            return true;
        }
        String field = sort.getField();
        Sort.Direction direction = sort.getDirection();
        return this.isValid(field) && this.isValid(direction);
    }

    private boolean isValid(String sortField) {
        return Certificate.Field.NAME.equals(sortField)
                || Certificate.Field.DESCRIPTION.equals(sortField)
                || Certificate.Field.PRICE.equals(sortField)
                || Certificate.Field.DURATION.equals(sortField)
                || Certificate.Field.CREATE_DATE.equals(sortField)
                || Certificate.Field.LAST_UPDATE_DATE.equals(sortField);
    }

    private boolean isValid(Sort.Direction direction) {
        return direction != null;
    }

}
