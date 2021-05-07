package com.epam.esm.persistence.entity;

public enum Permission {

    CERTIFICATE_CREATE("certificate:create"),
    CERTIFICATE_READ("certificate:read"),
    CERTIFICATE_UPDATE("certificate:update"),
    CERTIFICATE_DELETE("certificate:delete"),
    TAG_CREATE("tag:create"),
    TAG_READ("tag:read"),
    TAG_DELETE("tag:delete"),
    ACCOUNT_CREATE("account:create"),
    ACCOUNT_READ("account:read"),
    PURCHASE_CREATE("purchase:create"),
    PURCHASE_READ("purchase:read");


    private final String name;

    Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
