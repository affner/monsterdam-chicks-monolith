package com.monsterdam.app.domain;

import com.monsterdam.app.domain.enumeration.DocumentStatus;
import com.monsterdam.app.domain.enumeration.DocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A IdentityDocument.
 */
@Entity
@Table(name = "identity_document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdentityDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_name", nullable = false)
    private String documentName;

    @Column(name = "document_description")
    private String documentDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status")
    private DocumentStatus documentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Lob
    @Column(name = "file_document", nullable = false)
    private byte[] fileDocument;

    @NotNull
    @Column(name = "file_document_content_type", nullable = false)
    private String fileDocumentContentType;

    @NotNull
    @Column(name = "file_document_s_3_key", nullable = false)
    private String fileDocumentS3Key;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private IdentityDocumentReview review;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IdentityDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public IdentityDocument documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDescription() {
        return this.documentDescription;
    }

    public IdentityDocument documentDescription(String documentDescription) {
        this.setDocumentDescription(documentDescription);
        return this;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public DocumentStatus getDocumentStatus() {
        return this.documentStatus;
    }

    public IdentityDocument documentStatus(DocumentStatus documentStatus) {
        this.setDocumentStatus(documentStatus);
        return this;
    }

    public void setDocumentStatus(DocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public IdentityDocument documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public byte[] getFileDocument() {
        return this.fileDocument;
    }

    public IdentityDocument fileDocument(byte[] fileDocument) {
        this.setFileDocument(fileDocument);
        return this;
    }

    public void setFileDocument(byte[] fileDocument) {
        this.fileDocument = fileDocument;
    }

    public String getFileDocumentContentType() {
        return this.fileDocumentContentType;
    }

    public IdentityDocument fileDocumentContentType(String fileDocumentContentType) {
        this.fileDocumentContentType = fileDocumentContentType;
        return this;
    }

    public void setFileDocumentContentType(String fileDocumentContentType) {
        this.fileDocumentContentType = fileDocumentContentType;
    }

    public String getFileDocumentS3Key() {
        return this.fileDocumentS3Key;
    }

    public IdentityDocument fileDocumentS3Key(String fileDocumentS3Key) {
        this.setFileDocumentS3Key(fileDocumentS3Key);
        return this;
    }

    public void setFileDocumentS3Key(String fileDocumentS3Key) {
        this.fileDocumentS3Key = fileDocumentS3Key;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public IdentityDocument createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public IdentityDocument lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public IdentityDocument createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public IdentityDocument lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public IdentityDocumentReview getReview() {
        return this.review;
    }

    public void setReview(IdentityDocumentReview identityDocumentReview) {
        this.review = identityDocumentReview;
    }

    public IdentityDocument review(IdentityDocumentReview identityDocumentReview) {
        this.setReview(identityDocumentReview);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentityDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((IdentityDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentityDocument{" +
            "id=" + getId() +
            ", documentName='" + getDocumentName() + "'" +
            ", documentDescription='" + getDocumentDescription() + "'" +
            ", documentStatus='" + getDocumentStatus() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", fileDocument='" + getFileDocument() + "'" +
            ", fileDocumentContentType='" + getFileDocumentContentType() + "'" +
            ", fileDocumentS3Key='" + getFileDocumentS3Key() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
