package com.monsterdam.app.domain.enumeration;

/**
 * The UserLanguage enumeration.
 */
public enum UserLanguage {
    ES("user.language.es"),
    EN("user.language.en"),
    FR("user.language.fr");

    private final String value;

    UserLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
