package com.epam.esm.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "purchase")
public final class Purchase implements Identifiable {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_id_seq")
    @SequenceGenerator(name = "purchase_id_seq", sequenceName = "purchase_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "purchase_item",
            joinColumns = @JoinColumn(name = "purchase_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> items;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "date")
    private LocalDateTime date;

    protected Purchase() {
    }

    public Purchase(Long id,
                    Account account,
                    Set<Item> items,
                    BigDecimal cost,
                    LocalDateTime date) {
        this.id = id;
        this.account = account;
        this.items = items;
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

    public Set<Item> getItems() {
        return items == null
                ? null
                : Collections.unmodifiableSet(items);
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
                Objects.equals(items, purchase.items) &&
                Objects.equals(cost, purchase.cost) &&
                Objects.equals(date, purchase.date);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", account=" + account +
                ", items=" + items +
                ", cost=" + cost +
                ", date=" + date +
                '}';
    }


    public static final class Builder {

        private Long id;
        private Account account;
        private Set<Item> items;
        private BigDecimal cost;
        private LocalDateTime date;

        public Builder() {
        }

        private Builder(Purchase purchase) {
            id = purchase.id;
            account = purchase.account;
            items = purchase.items;
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

        public Builder setItems(Set<Item> items) {
            this.items = items;
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
            return new Purchase(id, account, items, cost, date);
        }

    }

    @Entity
    @Table(name = "item")
    public static final class Item {

        @Id
        @Column(name = "id", updatable = false)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
        @SequenceGenerator(name = "item_id_seq", sequenceName = "item_id_seq", allocationSize = 1)
        private Long id;

        @OneToOne
        @JoinColumn(name = "certificate_id")
        private Certificate certificate;

        @Column(name = "count")
        private Integer count;

        protected Item() {
        }

        public Item(Long id,
                    Certificate certificate,
                    Integer count) {
            this.id = id;
            this.certificate = certificate;
            this.count = count;
        }

        public static Builder builder() {
            return new Builder();
        }

        public Long getId() {
            return id;
        }

        public Certificate getCertificate() {
            return certificate;
        }

        public Integer getCount() {
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Item item = (Item) o;
            return Objects.equals(id, item.id) &&
                    Objects.equals(certificate, item.certificate) &&
                    Objects.equals(count, item.count);
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (certificate != null ? certificate.hashCode() : 0);
            result = 31 * result + (count != null ? count.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "{" +
                    "id=" + id +
                    ", certificate=" + certificate +
                    ", count=" + count +
                    '}';
        }


        public static final class Builder {

            private Long id;
            private Certificate certificate;
            private Integer count;

            public Builder() {
            }

            private Builder(Item item) {
                id = item.id;
                certificate = item.certificate;
                count = item.count;
            }

            public static Builder from(Item item) {
                return new Builder(item);
            }

            public Builder setId(Long id) {
                this.id = id;
                return this;
            }

            public Builder setCertificate(Certificate certificate) {
                this.certificate = certificate;
                return this;
            }

            public Builder setCount(Integer count) {
                this.count = count;
                return this;
            }

            public Item build() {
                return new Item(id, certificate, count);
            }

        }

    }

}
