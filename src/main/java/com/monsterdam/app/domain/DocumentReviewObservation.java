package com.monsterdam.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DocumentReviewObservation.
 */
@Entity
@Table(name = "document_review_observation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentReviewObservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "comment_date")
    private Instant commentDate;

    @NotNull
    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private IdentityDocumentReview review;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentReviewObservation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCommentDate() {
        return this.commentDate;
    }

    public DocumentReviewObservation commentDate(Instant commentDate) {
        this.setCommentDate(commentDate);
        return this;
    }

    public void setCommentDate(Instant commentDate) {
        this.commentDate = commentDate;
    }

    public String getComment() {
        return this.comment;
    }

    public DocumentReviewObservation comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public IdentityDocumentReview getReview() {
        return this.review;
    }

    public void setReview(IdentityDocumentReview identityDocumentReview) {
        this.review = identityDocumentReview;
    }

    public DocumentReviewObservation review(IdentityDocumentReview identityDocumentReview) {
        this.setReview(identityDocumentReview);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentReviewObservation)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentReviewObservation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentReviewObservation{" +
            "id=" + getId() +
            ", commentDate='" + getCommentDate() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
