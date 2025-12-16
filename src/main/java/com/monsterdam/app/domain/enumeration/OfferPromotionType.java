package com.monsterdam.app.domain.enumeration;

/**
 * The OfferPromotionType enumeration.
 */
public enum OfferPromotionType {
    DISCOUNT("offer.promotion.type.discount"),
    FREE_DAYS("offer.promotion.type.free-days");

    private final String value;

    OfferPromotionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
