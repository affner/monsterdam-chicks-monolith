package com.monsterdam.app.domain.enumeration;

/**
 * The ViewerWalletTransactionType enumeration.
 */
public enum ViewerWalletTransactionType {
    TOP_UP("viewer.wallet.transaction.type.top-up"),
    PURCHASE("viewer.wallet.transaction.type.purchase"),
    WITHDRAW("viewer.wallet.transaction.type.withdraw"),
    REFUND("viewer.wallet.transaction.type.refund");

    private final String value;

    ViewerWalletTransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
