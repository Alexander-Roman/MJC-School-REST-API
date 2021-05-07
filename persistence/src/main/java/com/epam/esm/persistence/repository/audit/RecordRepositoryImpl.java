package com.epam.esm.persistence.repository.audit;

import com.epam.esm.persistence.audit.entity.Record;
import com.epam.esm.persistence.repository.AbstractRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RecordRepositoryImpl extends AbstractRepository<Record> implements RecordRepository {

}
