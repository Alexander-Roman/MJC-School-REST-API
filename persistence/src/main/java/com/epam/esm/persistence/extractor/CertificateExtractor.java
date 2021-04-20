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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class CertificateExtractor implements ResultSetExtractor<Optional<Certificate>> {

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
    public Optional<Certificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Certificate certificate = null;
        Set<Tag> tags = new HashSet<>();

        while (resultSet.next()) {
            if (certificate == null) {
                Long certificateId = resultSet.getObject(COLUMN_CERTIFICATE_ID, Long.class);
                String certificateName = resultSet.getString(COLUMN_CERTIFICATE_NAME);
                String description = resultSet.getString(COLUMN_DESCRIPTION);
                BigDecimal price = resultSet.getObject(COLUMN_PRICE, BigDecimal.class);
                Integer duration = resultSet.getObject(COLUMN_DURATION, Integer.class);
                LocalDateTime createDate = resultSet.getObject(COLUMN_CREATE_DATE, LocalDateTime.class);
                LocalDateTime lastUpdateDate = resultSet.getObject(COLUMN_LAST_UPDATE_DATE, LocalDateTime.class);

                certificate = new Certificate(
                        certificateId,
                        certificateName,
                        description,
                        price,
                        duration,
                        createDate,
                        lastUpdateDate,
                        tags
                );
            }

            Long tagId = resultSet.getObject(COLUMN_TAG_ID, Long.class);
            if (tagId != null) {
                String tagName = resultSet.getString(COLUMN_TAG_NAME);
                Tag tag = new Tag(tagId, tagName, null);
                tags.add(tag);
            }
        }
        return Optional.ofNullable(certificate);
    }

}
