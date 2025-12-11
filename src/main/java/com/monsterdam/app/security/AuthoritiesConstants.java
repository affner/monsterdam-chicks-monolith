package com.monsterdam.app.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String SUPPORT_ADMIN = "ROLE_SUPPORT_ADMIN";

    public static final String SUPPORT = "ROLE_SUPPORT";

    public static final String FINANCE = "ROLE_FINANCE";

    public static final String CREATOR = "ROLE_CREATOR";

    public static final String VIEWER = "ROLE_VIEWER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}
}
