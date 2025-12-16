package com.monsterdam.app.domain.enumeration;

/**
 * The AuctionStatus enumeration.
 */
public enum AuctionStatus {
    ACTIVE("auction.status.active"),
    FINISHED("auction.status.finished"),
    CANCELLED("auction.status.cancelled");

    private final String value;

    AuctionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
