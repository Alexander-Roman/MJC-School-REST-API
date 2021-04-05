package com.epam.esm.persistence.clause;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.model.Sort;
import com.epam.esm.persistence.model.SortRequest;

import java.util.ArrayList;
import java.util.List;

public class SqlCertificateOrderClauseFactory {

    private static final String SQL_NO_ORDER_CLAUSE = "";
    private static final String TEMPLATE_SQL_ORDER_CLAUSE = "\n" +
            "ORDER BY %s \n";
    private static final String KEYWORD_DELIMITER = " ";
    private static final String PROPERTY_DELIMITER = ", ";

    private static final String COLUMN_CERTIFICATE_NAME = "certificate_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_LAST_UPDATE_DATE = "last_update_date";


    public static String buildSqlOrderClause(SortRequest sortRequest) {
        Sort primarySort = sortRequest.getSort();
        Sort secondarySort = sortRequest.getThenSort();
        if (primarySort == null && secondarySort == null) {
            return SQL_NO_ORDER_CLAUSE;
        }
        List<String> properties = new ArrayList<>();
        if (primarySort != null) {
            String property = renderToSqlProperty(primarySort);
            properties.add(property);
        }
        if (secondarySort != null) {
            String property = renderToSqlProperty(secondarySort);
            properties.add(property);
        }
        String conjunction = String.join(PROPERTY_DELIMITER, properties);
        return String.format(TEMPLATE_SQL_ORDER_CLAUSE, conjunction);

    }

    private static String renderToSqlProperty(Sort sort) {
        String field = sort.getField();
        String column = getMapping(field);
        Sort.Direction direction = sort.getDirection();
        String keyword = direction.getKeyword();
        return String.join(KEYWORD_DELIMITER, column, keyword);
    }

    private static String getMapping(String field) {
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
