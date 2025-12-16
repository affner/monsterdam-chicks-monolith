package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A PollOption.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "poll_option")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "option_description", nullable = false)
    private String optionDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "post" }, allowSetters = true)
    private PostPoll poll;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pollOption")
    @JsonIgnoreProperties(value = { "pollOption", "voter" }, allowSetters = true)
    private Set<PollVote> votes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PollOption id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionDescription() {
        return this.optionDescription;
    }

    public PollOption optionDescription(String optionDescription) {
        this.setOptionDescription(optionDescription);
        return this;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    public PostPoll getPoll() {
        return this.poll;
    }

    public void setPoll(PostPoll postPoll) {
        this.poll = postPoll;
    }

    public PollOption poll(PostPoll postPoll) {
        this.setPoll(postPoll);
        return this;
    }

    public Set<PollVote> getVotes() {
        return this.votes;
    }

    public void setVotes(Set<PollVote> pollVotes) {
        if (this.votes != null) {
            this.votes.forEach(i -> i.setPollOption(null));
        }
        if (pollVotes != null) {
            pollVotes.forEach(i -> i.setPollOption(this));
        }
        this.votes = pollVotes;
    }

    public PollOption votes(Set<PollVote> pollVotes) {
        this.setVotes(pollVotes);
        return this;
    }

    public PollOption addVotes(PollVote pollVote) {
        this.votes.add(pollVote);
        pollVote.setPollOption(this);
        return this;
    }

    public PollOption removeVotes(PollVote pollVote) {
        this.votes.remove(pollVote);
        pollVote.setPollOption(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollOption)) {
            return false;
        }
        return getId() != null && getId().equals(((PollOption) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollOption{" +
            "id=" + getId() +
            ", optionDescription='" + getOptionDescription() + "'" +
            "}";
    }
}
