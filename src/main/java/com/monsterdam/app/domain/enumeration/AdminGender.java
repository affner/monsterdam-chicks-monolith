package com.monsterdam.app.domain.enumeration;

/**
 * The AdminGender enumeration.
 */
public enum AdminGender {
    MALE("admin.gender.male"),
    FEMALE("admin.gender.female");

    private final String value;

    AdminGender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
