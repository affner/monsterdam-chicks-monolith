package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.GenericStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A PaymentProviderEvent.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payment_provider_event")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentProviderEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "provider_name", length = 50, nullable = false)
    private String providerName;

    @NotNull
    @Size(max = 80)
    @Column(name = "event_type", length = 80, nullable = false)
    private String eventType;

    @NotNull
    @Size(max = 120)
    @Column(name = "event_id", length = 120, nullable = false, unique = true)
    private String eventId;

    @Lob
    @Column(name = "payload_json", nullable = false)
    private String payloadJson;

    @NotNull
    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false)
    private GenericStatus processingStatus;

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

    public PaymentProviderEvent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return this.providerName;
    }

    public PaymentProviderEvent providerName(String providerName) {
        this.setProviderName(providerName);
        return this;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getEventType() {
        return this.eventType;
    }

    public PaymentProviderEvent eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventId() {
        return this.eventId;
    }

    public PaymentProviderEvent eventId(String eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPayloadJson() {
        return this.payloadJson;
    }

    public PaymentProviderEvent payloadJson(String payloadJson) {
        this.setPayloadJson(payloadJson);
        return this;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    public Instant getReceivedAt() {
        return this.receivedAt;
    }

    public PaymentProviderEvent receivedAt(Instant receivedAt) {
        this.setReceivedAt(receivedAt);
        return this;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Instant getProcessedAt() {
        return this.processedAt;
    }

    public PaymentProviderEvent processedAt(Instant processedAt) {
        this.setProcessedAt(processedAt);
        return this;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public GenericStatus getProcessingStatus() {
        return this.processingStatus;
    }

    public PaymentProviderEvent processingStatus(GenericStatus processingStatus) {
        this.setProcessingStatus(processingStatus);
        return this;
    }

    public void setProcessingStatus(GenericStatus processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PaymentProviderEvent createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PaymentProviderEvent lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public PaymentProviderEvent deletedDate(Instant deletedDate) {
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

    public PaymentProviderEvent payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentProviderEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentProviderEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentProviderEvent{" +
            "id=" + getId() +
            ", providerName='" + getProviderName() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", eventId='" + getEventId() + "'" +
            ", payloadJson='" + getPayloadJson() + "'" +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", processedAt='" + getProcessedAt() + "'" +
            ", processingStatus='" + getProcessingStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
