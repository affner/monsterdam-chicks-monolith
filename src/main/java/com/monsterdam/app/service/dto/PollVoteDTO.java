package com.monsterdam.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.PollVote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PollVoteDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant createdDate;

    @NotNull
    private PollOptionDTO pollOption;

    @NotNull
    private UserLiteDTO voter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public PollOptionDTO getPollOption() {
        return pollOption;
    }

    public void setPollOption(PollOptionDTO pollOption) {
        this.pollOption = pollOption;
    }

    public UserLiteDTO getVoter() {
        return voter;
    }

    public void setVoter(UserLiteDTO voter) {
        this.voter = voter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PollVoteDTO)) {
            return false;
        }

        PollVoteDTO pollVoteDTO = (PollVoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pollVoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PollVoteDTO{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", pollOption=" + getPollOption() +
            ", voter=" + getVoter() +
            "}";
    }
}
