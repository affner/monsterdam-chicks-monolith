package com.monsterdam.app.service.bff;

import org.springframework.data.jpa.domain.Specification;

public final class BffDeletedDate {

    private BffDeletedDate() {}

    public static <T> Specification<T> isNull() {
        return (root, query, cb) -> cb.isNull(root.get("deletedDate"));
    }
}
