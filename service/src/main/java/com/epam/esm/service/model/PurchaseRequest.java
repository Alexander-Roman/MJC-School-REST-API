package com.epam.esm.service.model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class PurchaseRequest {

    private final Long accountId;
    private final Map<Long, Integer> certificateIdQuantity;

    public PurchaseRequest(Long accountId, Map<Long,
            Integer> certificateIdQuantity) {
        this.accountId = accountId;
        this.certificateIdQuantity = certificateIdQuantity;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Map<Long, Integer> getCertificateIdQuantity() {
        return certificateIdQuantity == null
                ? null
                : Collections.unmodifiableMap(certificateIdQuantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchaseRequest that = (PurchaseRequest) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(certificateIdQuantity, that.certificateIdQuantity);
    }

    @Override
    public int hashCode() {
        int result = accountId != null ? accountId.hashCode() : 0;
        result = 31 * result + (certificateIdQuantity != null ? certificateIdQuantity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "accountId=" + accountId +
                ", certificateIdQuantity=" + certificateIdQuantity +
                '}';
    }

}
