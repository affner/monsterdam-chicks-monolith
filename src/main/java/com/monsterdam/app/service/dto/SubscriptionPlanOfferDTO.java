package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.OfferPromotionType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.SubscriptionPlanOffer} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionPlanOfferDTO implements Serializable {

    private Long id;

    private Duration freeDaysDuration;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100")
    private Float discountPercentage;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Integer subscriptionsLimit;

    @NotNull
    private OfferPromotionType promotionType;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    private UserLiteDTO creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Duration getFreeDaysDuration() {
        return freeDaysDuration;
    }

    public void setFreeDaysDuration(Duration freeDaysDuration) {
        this.freeDaysDuration = freeDaysDuration;
    }

    public Float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getSubscriptionsLimit() {
        return subscriptionsLimit;
    }

    public void setSubscriptionsLimit(Integer subscriptionsLimit) {
        this.subscriptionsLimit = subscriptionsLimit;
    }

    public OfferPromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(OfferPromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public UserLiteDTO getCreator() {
        return creator;
    }

    public void setCreator(UserLiteDTO creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionPlanOfferDTO)) {
            return false;
        }

        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO = (SubscriptionPlanOfferDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionPlanOfferDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionPlanOfferDTO{" +
            "id=" + getId() +
            ", freeDaysDuration='" + getFreeDaysDuration() + "'" +
            ", discountPercentage=" + getDiscountPercentage() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", subscriptionsLimit=" + getSubscriptionsLimit() +
            ", promotionType='" + getPromotionType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", creator=" + getCreator() +
            "}";
    }
}
