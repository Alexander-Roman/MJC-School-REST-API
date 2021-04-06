package com.epam.esm.persistence.mapper;

import com.epam.esm.persistence.entity.CertificateTag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CertificateTagRowMapper implements RowMapper<CertificateTag> {

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CERTIFICATE_ID = "certificate_id";
    private static final String COLUMN_TAG_ID = "tag_id";

    @Override
    public CertificateTag mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Long id = resultSet.getObject(COLUMN_ID, Long.class);
        Long certificateId = resultSet.getObject(COLUMN_CERTIFICATE_ID, Long.class);
        Long tagId = resultSet.getObject(COLUMN_TAG_ID, Long.class);
        return new CertificateTag(id, certificateId, tagId);
    }

}
