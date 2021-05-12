package com.epam.esm.persistence.repository.audit;

import com.epam.esm.persistence.audit.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for audit Record entity
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

}
