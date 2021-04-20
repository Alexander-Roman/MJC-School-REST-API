package com.epam.esm.persistence.extractor;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CertificateListExtractor implements ResultSetExtractor<List<Certificate>> {

    private static final String COLUMN_CERTIFICATE_ID = "certificate_id";
    private static final String COLUMN_CERTIFICATE_NAME = "certificate_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_LAST_UPDATE_DATE = "last_update_date";
    private static final String COLUMN_TAG_ID = "tag_id";
    private static final String COLUMN_TAG_NAME = "tag_name";

    @Override
    public List<Certificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, Certificate> mappedCertificates = new LinkedHashMap<>();
        Map<Long, Set<Tag>> mappedTags = new HashMap<>();

        while (resultSet.next()) {
            Long certificateId = resultSet.getObject(COLUMN_CERTIFICATE_ID, Long.class);

            if (mappedCertificates.get(certificateId) == null) {
                String certificateName = resultSet.getString(COLUMN_CERTIFICATE_NAME);
                String description = resultSet.getString(COLUMN_DESCRIPTION);
                BigDecimal price = resultSet.getObject(COLUMN_PRICE, BigDecimal.class);
                Integer duration = resultSet.getObject(COLUMN_DURATION, Integer.class);
                LocalDateTime createDate = resultSet.getObject(COLUMN_CREATE_DATE, LocalDateTime.class);
                LocalDateTime lastUpdateDate = resultSet.getObject(COLUMN_LAST_UPDATE_DATE, LocalDateTime.class);
                Set<Tag> tags = new HashSet<>();

                Certificate certificate = new Certificate(
                        certificateId,
                        certificateName,
                        description,
                        price,
                        duration,
                        createDate,
                        lastUpdateDate,
                        tags
                );

                mappedCertificates.put(certificateId, certificate);
                mappedTags.put(certificateId, tags);
            }

            Long tagId = resultSet.getObject(COLUMN_TAG_ID, Long.class);
            if (tagId != null) {
                String tagName = resultSet.getString(COLUMN_TAG_NAME);
                Tag tag = new Tag(tagId, tagName, null);
                Set<Tag> tags = mappedTags.get(certificateId);
                tags.add(tag);
            }

        }

        Collection<Certificate> values = mappedCertificates.values();
        List<Certificate> results = new ArrayList<>(values);
        return Collections.unmodifiableList(results);
    }

}
