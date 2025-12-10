package com.monsterdam.app.domain.enumeration;

/**
 * The MoneyEarningType enumeration.
 */
public enum MoneyEarningType {
    TIP_EARNING("money.earnings.type.tip-earning"),
    SUBSCRIPTION_EARNING("money.earnings.type.subscription-earning"),
    POST_EARNING("money.earnings.type.post-earning"),
    MESSAGE_EARNING("money.earnings.type.message-earning"),
    WITHDRAW("money.earnings.type.withdraw");

    private final String value;

    MoneyEarningType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
