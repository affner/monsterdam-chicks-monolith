package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A PostPoll.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "post_poll")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostPoll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "question", nullable = false)
    private String question;

    @NotNull
    @Column(name = "is_multi_choice", nullable = false)
    private Boolean isMultiChoice;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Duration duration;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "creator", "polls" }, allowSetters = true)
    private PostFeed post;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostPoll id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public PostPoll question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getIsMultiChoice() {
        return this.isMultiChoice;
    }

    public PostPoll isMultiChoice(Boolean isMultiChoice) {
        this.setIsMultiChoice(isMultiChoice);
        return this;
    }

    public void setIsMultiChoice(Boolean isMultiChoice) {
        this.isMultiChoice = isMultiChoice;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public PostPoll endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public PostPoll duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PostPoll createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PostPoll lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PostPoll createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PostPoll lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public PostPoll deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public PostFeed getPost() {
        return this.post;
    }

    public void setPost(PostFeed postFeed) {
        this.post = postFeed;
    }

    public PostPoll post(PostFeed postFeed) {
        this.setPost(postFeed);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostPoll)) {
            return false;
        }
        return getId() != null && getId().equals(((PostPoll) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostPoll{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", isMultiChoice='" + getIsMultiChoice() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", duration='" + getDuration() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
