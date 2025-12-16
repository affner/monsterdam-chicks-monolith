package com.monsterdam.app.domain.enumeration;

/**
 * The MoneyWithdrawStatus enumeration.
 */
public enum MoneyWithdrawStatus {
    WITHDRAW_PENDING("money.withdraw.status.pending"),
    WITHDRAW_PROCESSED("money.withdraw.status.processed"),
    WITHDRAW_DECLINED("money.withdraw.status.declined");

    private final String value;

    MoneyWithdrawStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
