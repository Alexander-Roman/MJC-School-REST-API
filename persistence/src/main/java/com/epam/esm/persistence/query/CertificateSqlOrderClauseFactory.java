package com.epam.esm.persistence.query;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.Sort;
import com.epam.esm.persistence.model.SortRequest;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.LinkedList;

@Component
public class CertificateSqlOrderClauseFactory implements SqlOrderClauseFactory<Certificate> {

    private static final String TEMPLATE_SQL_ORDER_BY_CLAUSE = "ORDER BY %s, %s \n";
    private static final String DEFAULT_SORT_PROPERTY = "last_update_date DESC";
    private static final String DEFAULT_THEN_SORT_PROPERTY = "certificate_name ASC";
    private static final String KEYWORD_DELIMITER = " ";

    private static final String COLUMN_CERTIFICATE_NAME = "certificate_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_LAST_UPDATE_DATE = "last_update_date";

    @Override
    public String getOrderClause(SortRequest sortRequest) {
        Deque<String> sorts = new LinkedList<>();

        Sort primarySort = sortRequest.getSort();
        if (primarySort != null) {
            String sort = this.renderToSqlProperty(primarySort);
            sorts.offer(sort);
        }
        Sort secondarySort = sortRequest.getThenSort();
        if (secondarySort != null) {
            String sort = this.renderToSqlProperty(secondarySort);
            sorts.offer(sort);
        }

        sorts.offer(DEFAULT_SORT_PROPERTY);
        sorts.offer(DEFAULT_THEN_SORT_PROPERTY);

        String sort = sorts.poll();
        String thenSort = sorts.poll();
        return String.format(TEMPLATE_SQL_ORDER_BY_CLAUSE, sort, thenSort);
    }

    private String renderToSqlProperty(Sort sort) {
        String field = sort.getField();
        String column = this.getMapping(field);
        Sort.Direction direction = sort.getDirection();
        String keyword = direction.getKeyword();
        return String.join(KEYWORD_DELIMITER, column, keyword);
    }

    private String getMapping(String field) {
        switch (field) {
            case Certificate.Field.NAME:
                return COLUMN_CERTIFICATE_NAME;
            case Certificate.Field.DESCRIPTION:
                return COLUMN_DESCRIPTION;
            case Certificate.Field.PRICE:
                return COLUMN_PRICE;
            case Certificate.Field.DURATION:
                return COLUMN_DURATION;
            case Certificate.Field.CREATE_DATE:
                return COLUMN_CREATE_DATE;
            case Certificate.Field.LAST_UPDATE_DATE:
                return COLUMN_LAST_UPDATE_DATE;
            default:
                throw new IllegalArgumentException("Sort field is unknown: " + field);
        }
    }

}
