package com.epam.esm.persistence.extractor;

import com.epam.esm.persistence.entity.CertificateTag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateTagListExtractor implements ResultSetExtractor<List<CertificateTag>> {

    private static final String COLUMN_CERTIFICATE_TAG_ID = "certificate_tag_id";
    private static final String COLUMN_CERTIFICATE_ID = "certificate_id";
    private static final String COLUMN_TAG_ID = "tag_id";

    @Override
    public List<CertificateTag> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<CertificateTag> certificateTags = new ArrayList<>();
        while (resultSet.next()) {
            Long id = resultSet.getObject(COLUMN_CERTIFICATE_TAG_ID, Long.class);
            Long certificateId = resultSet.getObject(COLUMN_CERTIFICATE_ID, Long.class);
            Long tagId = resultSet.getObject(COLUMN_TAG_ID, Long.class);
            CertificateTag certificateTag = new CertificateTag(id, certificateId, tagId);
            certificateTags.add(certificateTag);
        }
        return certificateTags;
    }

}
