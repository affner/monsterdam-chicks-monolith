package com.monsterdam.app.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A PollOption.
 */
@Entity
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

    @NotNull
    @Column(name = "vote_count", nullable = false)
    private Integer voteCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private PostPoll poll;

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

    public Integer getVoteCount() {
        return this.voteCount;
    }

    public PollOption voteCount(Integer voteCount) {
        this.setVoteCount(voteCount);
        return this;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
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
            ", voteCount=" + getVoteCount() +
            "}";
    }
}
