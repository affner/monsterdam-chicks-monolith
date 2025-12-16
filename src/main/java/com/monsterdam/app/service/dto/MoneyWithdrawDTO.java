package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.MoneyWithdrawStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.MoneyWithdraw} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyWithdrawDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Size(max = 3)
    private String currency;

    @Size(max = 50)
    private String payoutProviderName;

    @Size(max = 100)
    private String payoutReferenceId;

    private Instant processedAt;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    @NotNull
    private MoneyWithdrawStatus withdrawStatus;

    private UserLiteDTO creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayoutProviderName() {
        return payoutProviderName;
    }

    public void setPayoutProviderName(String payoutProviderName) {
        this.payoutProviderName = payoutProviderName;
    }

    public String getPayoutReferenceId() {
        return payoutReferenceId;
    }

    public void setPayoutReferenceId(String payoutReferenceId) {
        this.payoutReferenceId = payoutReferenceId;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
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

    public MoneyWithdrawStatus getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(MoneyWithdrawStatus withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
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
        if (!(o instanceof MoneyWithdrawDTO)) {
            return false;
        }

        MoneyWithdrawDTO moneyWithdrawDTO = (MoneyWithdrawDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moneyWithdrawDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyWithdrawDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", payoutProviderName='" + getPayoutProviderName() + "'" +
            ", payoutReferenceId='" + getPayoutReferenceId() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", withdrawStatus='" + getWithdrawStatus() + "'" +
            ", creator=" + getCreator() +
            "}";
    }
}
