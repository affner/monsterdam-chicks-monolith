package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.GenericStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A RefundTransaction.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "refund_transaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RefundTransaction implements Serializable {

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

    @Size(max = 255)
    @Column(name = "reason", length = 255)
    private String reason;

    @Size(max = 100)
    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Size(max = 100)
    @Column(name = "provider_charge_id", length = 100)
    private String providerChargeId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GenericStatus status;

    @NotNull
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "processed_at")
    private Instant processedAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "method", "provider", "viewer" }, allowSetters = true)
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RefundTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public RefundTransaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public RefundTransaction currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReason() {
        return this.reason;
    }

    public RefundTransaction reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPaymentReference() {
        return this.paymentReference;
    }

    public RefundTransaction paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getProviderChargeId() {
        return this.providerChargeId;
    }

    public RefundTransaction providerChargeId(String providerChargeId) {
        this.setProviderChargeId(providerChargeId);
        return this;
    }

    public void setProviderChargeId(String providerChargeId) {
        this.providerChargeId = providerChargeId;
    }

    public GenericStatus getStatus() {
        return this.status;
    }

    public RefundTransaction status(GenericStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(GenericStatus status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public RefundTransaction createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getProcessedAt() {
        return this.processedAt;
    }

    public RefundTransaction processedAt(Instant processedAt) {
        this.setProcessedAt(processedAt);
        return this;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public RefundTransaction createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public RefundTransaction lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public RefundTransaction deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public RefundTransaction payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RefundTransaction)) {
            return false;
        }
        return getId() != null && getId().equals(((RefundTransaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RefundTransaction{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", reason='" + getReason() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", providerChargeId='" + getProviderChargeId() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
