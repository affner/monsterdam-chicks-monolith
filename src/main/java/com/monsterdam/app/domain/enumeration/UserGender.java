package com.monsterdam.app.domain.enumeration;

/**
 * The UserGender enumeration.
 */
public enum UserGender {
    MALE("user.gender.male"),
    FEMALE("user.gender.female");

    private final String value;

    UserGender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
