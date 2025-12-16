package com.monsterdam.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A PaymentProvider.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payment_provider")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "api_key_text", nullable = false)
    private String apiKeyText;

    @NotNull
    @Column(name = "api_secret_text", nullable = false)
    private String apiSecretText;

    @NotNull
    @Column(name = "endpoint_text", nullable = false)
    private String endpointText;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentProvider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProviderName() {
        return this.providerName;
    }

    public PaymentProvider providerName(String providerName) {
        this.setProviderName(providerName);
        return this;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDescription() {
        return this.description;
    }

    public PaymentProvider description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiKeyText() {
        return this.apiKeyText;
    }

    public PaymentProvider apiKeyText(String apiKeyText) {
        this.setApiKeyText(apiKeyText);
        return this;
    }

    public void setApiKeyText(String apiKeyText) {
        this.apiKeyText = apiKeyText;
    }

    public String getApiSecretText() {
        return this.apiSecretText;
    }

    public PaymentProvider apiSecretText(String apiSecretText) {
        this.setApiSecretText(apiSecretText);
        return this;
    }

    public void setApiSecretText(String apiSecretText) {
        this.apiSecretText = apiSecretText;
    }

    public String getEndpointText() {
        return this.endpointText;
    }

    public PaymentProvider endpointText(String endpointText) {
        this.setEndpointText(endpointText);
        return this;
    }

    public void setEndpointText(String endpointText) {
        this.endpointText = endpointText;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PaymentProvider createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PaymentProvider lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PaymentProvider createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PaymentProvider lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public PaymentProvider deletedDate(Instant deletedDate) {
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
        if (!(o instanceof PaymentProvider)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentProvider) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentProvider{" +
            "id=" + getId() +
            ", providerName='" + getProviderName() + "'" +
            ", description='" + getDescription() + "'" +
            ", apiKeyText='" + getApiKeyText() + "'" +
            ", apiSecretText='" + getApiSecretText() + "'" +
            ", endpointText='" + getEndpointText() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
