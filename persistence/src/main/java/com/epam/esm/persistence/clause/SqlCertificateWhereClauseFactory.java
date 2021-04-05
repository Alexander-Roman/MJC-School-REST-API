package com.epam.esm.persistence.clause;

import com.epam.esm.persistence.model.FilterRequest;

import java.util.ArrayList;
import java.util.List;

public class SqlCertificateWhereClauseFactory {

    private static final String SQL_NO_WHERE_CLAUSE = "";
    private static final String TEMPLATE_SQL_WHERE_CLAUSE = "\n" +
            "WHERE %s \n";
    private static final String SQL_FILTER_BY_SEARCH_STATEMENT =
            "(certificate.name ILIKE CONCAT('%', :search, '%') OR certificate.description ILIKE CONCAT('%', :search, '%'))";
    private static final String SQL_FILTER_BY_TAG_NAME_STATEMENT =
            "tag.name = :tagName";
    private static final String STATEMENTS_DELIMITER = " AND ";


    public static String buildSqlWhereClause(FilterRequest filterRequest) {
        String search = filterRequest.getSearch();
        String tagName = filterRequest.getTagName();
        if (search == null && tagName == null) {
            return SQL_NO_WHERE_CLAUSE;
        }

        List<String> statements = new ArrayList<>();
        if (search != null) {
            statements.add(SQL_FILTER_BY_SEARCH_STATEMENT);
        }
        if (tagName != null) {
            statements.add(SQL_FILTER_BY_TAG_NAME_STATEMENT);
        }
        String conjunction = String.join(STATEMENTS_DELIMITER, statements);
        return String.format(TEMPLATE_SQL_WHERE_CLAUSE, conjunction);
    }

}
