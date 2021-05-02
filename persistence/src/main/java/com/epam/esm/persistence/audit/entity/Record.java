package com.epam.esm.persistence.audit.entity;

import com.epam.esm.persistence.entity.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "audit_record")
public class Record implements Identifiable {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_record_id_seq")
    @SequenceGenerator(name = "audit_record_id_seq", sequenceName = "audit_record_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "operation")
    @Enumerated(EnumType.STRING)
    private Operation operation;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "entry")
    private String entry;

    protected Record() {
    }

    public Record(Long id,
                  Operation operation,
                  LocalDateTime date,
                  String entry) {
        this.id = id;
        this.operation = operation;
        this.date = date;
        this.entry = entry;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Operation getOperation() {
        return operation;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getEntry() {
        return entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Record record = (Record) o;
        return Objects.equals(id, record.id) &&
                operation == record.operation &&
                Objects.equals(date, record.date) &&
                Objects.equals(entry, record.entry);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (entry != null ? entry.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", operation=" + operation +
                ", date=" + date +
                ", entry='" + entry + '\'' +
                '}';
    }


    public enum Operation {
        CREATE, UPDATE, DELETE
    }

}
