package com.monsterdam.app.domain;

import com.monsterdam.app.domain.enumeration.LedgerAccountType;
import com.monsterdam.app.domain.enumeration.LedgerEntryType;
import com.monsterdam.app.domain.enumeration.LedgerReason;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A LedgerEntry.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ledger_entry")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LedgerEntry implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false)
    private LedgerEntryType entryType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private LedgerAccountType accountType;

    @Column(name = "account_owner_id")
    private Long accountOwnerId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private LedgerReason reason;

    @NotNull
    @Size(max = 40)
    @Column(name = "reference_type", length = 40, nullable = false)
    private String referenceType;

    @NotNull
    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @NotNull
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LedgerEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public LedgerEntry amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public LedgerEntry currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LedgerEntryType getEntryType() {
        return this.entryType;
    }

    public LedgerEntry entryType(LedgerEntryType entryType) {
        this.setEntryType(entryType);
        return this;
    }

    public void setEntryType(LedgerEntryType entryType) {
        this.entryType = entryType;
    }

    public LedgerAccountType getAccountType() {
        return this.accountType;
    }

    public LedgerEntry accountType(LedgerAccountType accountType) {
        this.setAccountType(accountType);
        return this;
    }

    public void setAccountType(LedgerAccountType accountType) {
        this.accountType = accountType;
    }

    public Long getAccountOwnerId() {
        return this.accountOwnerId;
    }

    public LedgerEntry accountOwnerId(Long accountOwnerId) {
        this.setAccountOwnerId(accountOwnerId);
        return this;
    }

    public void setAccountOwnerId(Long accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    public LedgerReason getReason() {
        return this.reason;
    }

    public LedgerEntry reason(LedgerReason reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(LedgerReason reason) {
        this.reason = reason;
    }

    public String getReferenceType() {
        return this.referenceType;
    }

    public LedgerEntry referenceType(String referenceType) {
        this.setReferenceType(referenceType);
        return this;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Long getReferenceId() {
        return this.referenceId;
    }

    public LedgerEntry referenceId(Long referenceId) {
        this.setReferenceId(referenceId);
        return this;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public LedgerEntry createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public LedgerEntry createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public LedgerEntry deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LedgerEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((LedgerEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LedgerEntry{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", entryType='" + getEntryType() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", accountOwnerId=" + getAccountOwnerId() +
            ", reason='" + getReason() + "'" +
            ", referenceType='" + getReferenceType() + "'" +
            ", referenceId=" + getReferenceId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
