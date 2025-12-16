package com.monsterdam.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A PaymentMethod.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "payment_method")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "method_name", nullable = false)
    private String methodName;

    @NotNull
    @Size(max = 100)
    @Column(name = "token_text", length = 100, nullable = false)
    private String tokenText;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

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

    public PaymentMethod id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public PaymentMethod methodName(String methodName) {
        this.setMethodName(methodName);
        return this;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getTokenText() {
        return this.tokenText;
    }

    public PaymentMethod tokenText(String tokenText) {
        this.setTokenText(tokenText);
        return this;
    }

    public void setTokenText(String tokenText) {
        this.tokenText = tokenText;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public PaymentMethod expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PaymentMethod createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PaymentMethod lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PaymentMethod createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PaymentMethod lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public PaymentMethod deletedDate(Instant deletedDate) {
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
        if (!(o instanceof PaymentMethod)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentMethod) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentMethod{" +
            "id=" + getId() +
            ", methodName='" + getMethodName() + "'" +
            ", tokenText='" + getTokenText() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
