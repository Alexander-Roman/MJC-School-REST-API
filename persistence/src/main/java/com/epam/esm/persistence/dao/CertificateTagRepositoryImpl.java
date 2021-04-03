package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.query.UpdateQuery;
import com.epam.esm.persistence.query.certificatetag.AddNewTagsQuery;
import com.epam.esm.persistence.query.certificatetag.DeleteByCertificateIdQuery;
import com.epam.esm.persistence.query.certificatetag.RemoveOldTagsByNamesQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CertificateTagRepositoryImpl extends AbstractRepository<CertificateTag> implements CertificateTagRepository {

    @Autowired
    public CertificateTagRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                        ResultSetExtractor<List<CertificateTag>> resultSetExtractor) {
        super(namedParameterJdbcTemplate, resultSetExtractor);
    }

    @Override
    public void resolveAddedTags(Certificate certificate) {
        UpdateQuery<CertificateTag> query = new AddNewTagsQuery(certificate);
        this.executeUpdate(query);
    }

    @Override
    public void resolveRemovedTags(Certificate certificate) {
        UpdateQuery<CertificateTag> query = new RemoveOldTagsByNamesQuery(certificate);
        this.executeUpdate(query);
    }

    @Override
    public void deleteByCertificateId(Long id) {
        UpdateQuery<CertificateTag> query = new DeleteByCertificateIdQuery(id);
        this.executeUpdate(query);
    }

}
