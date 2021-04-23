package com.epam.esm.persistence.repository;

import com.epam.esm.persistence.entity.Certificate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class CertificateRepositoryImpl extends AbstractRepository<Certificate> {

}
