package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.MoneyWithdrawStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A MoneyWithdraw.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "money_withdraw")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MoneyWithdraw implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Size(max = 3)
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Size(max = 50)
    @Column(name = "payout_provider_name", length = 50)
    private String payoutProviderName;

    @Size(max = 100)
    @Column(name = "payout_reference_id", length = 100)
    private String payoutReferenceId;

    @Column(name = "processed_at")
    private Instant processedAt;

    @NotNull
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "withdraw_status", nullable = false)
    private MoneyWithdrawStatus withdrawStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "profile",
            "settings",
            "countryOfBirth",
            "purchasedSubscriptionsAsViewers",
            "purchasedSubscriptionsAsCreators",
            "pollVotes",
            "associationsAsOwners",
            "assistanceTickets",
            "payments",
            "comments",
            "posts",
            "singleAudios",
            "singleVideos",
            "singlePhotos",
            "videoStories",
            "directMessagesSents",
            "likeMarks",
            "notificationsReceiveds",
            "notificationsTriggereds",
            "postMentions",
            "commentMentions",
            "chatRooms",
        },
        allowSetters = true
    )
    private UserLite creator;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoneyWithdraw id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public MoneyWithdraw amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public MoneyWithdraw currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayoutProviderName() {
        return this.payoutProviderName;
    }

    public MoneyWithdraw payoutProviderName(String payoutProviderName) {
        this.setPayoutProviderName(payoutProviderName);
        return this;
    }

    public void setPayoutProviderName(String payoutProviderName) {
        this.payoutProviderName = payoutProviderName;
    }

    public String getPayoutReferenceId() {
        return this.payoutReferenceId;
    }

    public MoneyWithdraw payoutReferenceId(String payoutReferenceId) {
        this.setPayoutReferenceId(payoutReferenceId);
        return this;
    }

    public void setPayoutReferenceId(String payoutReferenceId) {
        this.payoutReferenceId = payoutReferenceId;
    }

    public Instant getProcessedAt() {
        return this.processedAt;
    }

    public MoneyWithdraw processedAt(Instant processedAt) {
        this.setProcessedAt(processedAt);
        return this;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MoneyWithdraw createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public MoneyWithdraw lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MoneyWithdraw createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public MoneyWithdraw lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public MoneyWithdraw deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public MoneyWithdrawStatus getWithdrawStatus() {
        return this.withdrawStatus;
    }

    public MoneyWithdraw withdrawStatus(MoneyWithdrawStatus withdrawStatus) {
        this.setWithdrawStatus(withdrawStatus);
        return this;
    }

    public void setWithdrawStatus(MoneyWithdrawStatus withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public UserLite getCreator() {
        return this.creator;
    }

    public void setCreator(UserLite userLite) {
        this.creator = userLite;
    }

    public MoneyWithdraw creator(UserLite userLite) {
        this.setCreator(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoneyWithdraw)) {
            return false;
        }
        return getId() != null && getId().equals(((MoneyWithdraw) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoneyWithdraw{" +
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
            "}";
    }
}
