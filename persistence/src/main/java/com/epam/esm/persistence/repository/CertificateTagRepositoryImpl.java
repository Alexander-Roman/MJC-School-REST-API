package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Certificate;
import com.epam.esm.persistence.entity.CertificateTag;
import com.epam.esm.persistence.query.UpdateQuery;
import com.epam.esm.persistence.query.certificatetag.PgResolveAddedTagsByNamesQuery;
import com.epam.esm.persistence.query.certificatetag.DeleteByCertificateIdQuery;
import com.epam.esm.persistence.query.certificatetag.DeleteByTagIdQuery;
import com.epam.esm.persistence.query.certificatetag.ResolveRemovedTagsByNamesQuery;
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
    public void resolveAddedTagsByNames(Certificate certificate) {
        UpdateQuery<CertificateTag> query = new PgResolveAddedTagsByNamesQuery(certificate);
        this.executeUpdate(query);
    }

    @Override
    public void resolveRemovedTagsByNames(Certificate certificate) {
        UpdateQuery<CertificateTag> query = new ResolveRemovedTagsByNamesQuery(certificate);
        this.executeUpdate(query);
    }

    @Override
    public void deleteByCertificateId(Long id) {
        UpdateQuery<CertificateTag> query = new DeleteByCertificateIdQuery(id);
        this.executeUpdate(query);
    }

    @Override
    public void deleteByTagId(Long id) {
        UpdateQuery<CertificateTag> query = new DeleteByTagIdQuery(id);
        this.executeUpdate(query);
    }

}
