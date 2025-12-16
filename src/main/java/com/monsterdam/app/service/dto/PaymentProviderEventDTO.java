package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.GenericStatus;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.PaymentProviderEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentProviderEventDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String providerName;

    @NotNull
    @Size(max = 80)
    private String eventType;

    @NotNull
    @Size(max = 120)
    private String eventId;

    @Lob
    private String payloadJson;

    @NotNull
    private Instant receivedAt;

    private Instant processedAt;

    @NotNull
    private GenericStatus processingStatus;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    private PaymentDTO payment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public GenericStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(GenericStatus processingStatus) {
        this.processingStatus = processingStatus;
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

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentProviderEventDTO)) {
            return false;
        }

        PaymentProviderEventDTO paymentProviderEventDTO = (PaymentProviderEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentProviderEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentProviderEventDTO{" +
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
            ", payment=" + getPayment() +
            "}";
    }
}
