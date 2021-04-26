package com.epam.esm.persistence.entity;

import java.util.Collections;
import java.util.Set;

public enum Role {

    USER(Collections.emptySet()),
    ADMIN(Collections.emptySet());

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

}
