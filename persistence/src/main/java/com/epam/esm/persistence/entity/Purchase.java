package com.epam.esm.persistence.entity;

import com.epam.esm.persistence.audit.listener.AuditListener;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "purchase")
@EntityListeners(AuditListener.class)
public class Purchase implements Identifiable {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_id_seq")
    @SequenceGenerator(name = "purchase_id_seq", sequenceName = "purchase_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ElementCollection
    @CollectionTable(name = "purchase_item",
            joinColumns = {@JoinColumn(name = "purchase_id", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "certificate_id")
    @Column(name = "quantity")
    private Map<Certificate, Integer> certificateQuantity;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "date")
    @CreationTimestamp
    private LocalDateTime date;

    protected Purchase() {
    }

    public Purchase(Long id,
                    Account account,
                    Map<Certificate, Integer> certificateQuantity,
                    BigDecimal cost,
                    LocalDateTime date) {
        this.id = id;
        this.account = account;
        this.certificateQuantity = certificateQuantity;
        this.cost = cost;
        this.date = date;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public Map<Certificate, Integer> getCertificateQuantity() {
        return certificateQuantity == null
                ? null
                : Collections.unmodifiableMap(certificateQuantity);
    }

    public BigDecimal getCost() {
        return cost;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Purchase purchase = (Purchase) o;
        return Objects.equals(id, purchase.id) &&
                Objects.equals(account, purchase.account) &&
                Objects.equals(certificateQuantity, purchase.certificateQuantity) &&
                Objects.equals(cost, purchase.cost) &&
                Objects.equals(date, purchase.date);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (certificateQuantity != null ? certificateQuantity.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", account=" + account +
                ", certificateQuantity=" + certificateQuantity +
                ", cost=" + cost +
                ", date=" + date +
                '}';
    }


    public static final class Builder {

        private Long id;
        private Account account;
        private Map<Certificate, Integer> certificateQuantity;
        private BigDecimal cost;
        private LocalDateTime date;

        public Builder() {
        }

        private Builder(Purchase purchase) {
            id = purchase.id;
            account = purchase.account;
            certificateQuantity = purchase.certificateQuantity;
            cost = purchase.cost;
            date = purchase.date;
        }

        public static Builder from(Purchase purchase) {
            return new Builder(purchase);
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setAccount(Account account) {
            this.account = account;
            return this;
        }

        public Builder setCertificateQuantity(Map<Certificate, Integer> certificateQuantity) {
            this.certificateQuantity = certificateQuantity;
            return this;
        }

        public Builder setCost(BigDecimal cost) {
            this.cost = cost;
            return this;
        }

        public Builder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Purchase build() {
            return new Purchase(id, account, certificateQuantity, cost, date);
        }

    }

}
