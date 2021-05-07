package com.epam.esm.persistence.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum Role {

    USER(
            new HashSet<>(
                    Arrays.asList(
                            Permission.CERTIFICATE_READ,
                            Permission.TAG_READ,
                            Permission.ACCOUNT_READ,
                            Permission.PURCHASE_READ,
                            Permission.PURCHASE_CREATE
                    )
            )
    ),
    ADMIN(
            new HashSet<>(
                    Arrays.asList(
                            Permission.CERTIFICATE_CREATE,
                            Permission.CERTIFICATE_READ,
                            Permission.CERTIFICATE_UPDATE,
                            Permission.CERTIFICATE_DELETE,
                            Permission.TAG_CREATE,
                            Permission.TAG_READ,
                            Permission.TAG_DELETE,
                            Permission.ACCOUNT_CREATE,
                            Permission.ACCOUNT_READ,
                            Permission.PURCHASE_CREATE,
                            Permission.PURCHASE_READ
                    )
            )
    );

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

}
