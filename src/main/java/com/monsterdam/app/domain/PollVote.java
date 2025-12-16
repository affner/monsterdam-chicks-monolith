package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A PollVote.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "poll_vote")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "poll", "votes" }, allowSetters = true)
    private PollOption pollOption;

    @ManyToOne(optional = false)
    @NotNull
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
    private UserLite voter;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PollVote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PollVote createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public PollOption getPollOption() {
        return this.pollOption;
    }

    public void setPollOption(PollOption pollOption) {
        this.pollOption = pollOption;
    }

    public PollVote pollOption(PollOption pollOption) {
        this.setPollOption(pollOption);
        return this;
    }

    public UserLite getVoter() {
        return this.voter;
    }

    public void setVoter(UserLite userLite) {
        this.voter = userLite;
    }

    public PollVote voter(UserLite userLite) {
        this.setVoter(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollVote)) {
            return false;
        }
        return getId() != null && getId().equals(((PollVote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollVote{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
