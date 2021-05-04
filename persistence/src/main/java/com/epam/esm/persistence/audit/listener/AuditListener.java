package com.epam.esm.persistence.audit.listener;

import com.epam.esm.persistence.audit.entity.Record;
import com.epam.esm.persistence.entity.Identifiable;
import com.epam.esm.persistence.repository.audit.RecordRepository;
import com.epam.esm.persistence.repository.audit.RecordRepositoryFactory;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class AuditListener {

    private RecordRepository recordRepository;

    @PrePersist
    public void prePersist(Identifiable identifiable) {
        this.registerOperation(identifiable, Record.Operation.CREATE);
    }

    @PreUpdate
    public void preUpdate(Identifiable identifiable) {
        this.registerOperation(identifiable, Record.Operation.UPDATE);
    }

    @PreRemove
    public void preRemove(Identifiable identifiable) {
        this.registerOperation(identifiable, Record.Operation.DELETE);
    }

    private void registerOperation(Identifiable identifiable, Record.Operation operation) {
        if (recordRepository == null) {
            recordRepository = RecordRepositoryFactory.getRecordRepository();
        }
        String entry = identifiable.toString();
        Record record = new Record(null, operation, null, entry);
        recordRepository.save(record);
    }

}
