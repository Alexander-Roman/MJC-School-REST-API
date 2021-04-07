package com.epam.esm.persistence.clause;

import com.epam.esm.persistence.model.FilterRequest;

import java.util.ArrayList;
import java.util.List;

public class SqlCertificateWhereClauseBuilder {

    private static final String SQL_NO_WHERE_CLAUSE = "";
    private static final String TEMPLATE_SQL_WHERE_CLAUSE = "\n" +
            "WHERE %s \n";
    private static final String SQL_FILTER_BY_SEARCH_STATEMENT =
            "(certificate.name ILIKE CONCAT('%', :search, '%') OR certificate.description ILIKE CONCAT('%', :search, '%'))";
    private static final String SQL_FILTER_BY_TAG_NAME_STATEMENT = "\n" +
            "certificate.id IN ( \n" +
            "   SELECT DISTINCT certificate.id \n" +
            "   FROM certificate \n" +
            "            LEFT JOIN certificate_tag ON certificate.id = certificate_tag.certificate_id \n" +
            "            LEFT JOIN tag ON certificate_tag.tag_id = tag.id \n" +
            "   WHERE tag.name = :tagName \n" +
            ") \n";
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
