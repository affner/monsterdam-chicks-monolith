package com.monsterdam.app.domain;

import com.monsterdam.app.domain.enumeration.ModerationActionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A ModerationAction.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "moderation_action")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModerationAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ModerationActionType actionType;

    @Size(max = 255)
    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "action_date")
    private Instant actionDate;

    @Column(name = "duration_days")
    private Duration durationDays;

    @Column(name = "ticket_id")
    private Long ticketId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ModerationAction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModerationActionType getActionType() {
        return this.actionType;
    }

    public ModerationAction actionType(ModerationActionType actionType) {
        this.setActionType(actionType);
        return this;
    }

    public void setActionType(ModerationActionType actionType) {
        this.actionType = actionType;
    }

    public String getReason() {
        return this.reason;
    }

    public ModerationAction reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getActionDate() {
        return this.actionDate;
    }

    public ModerationAction actionDate(Instant actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public Duration getDurationDays() {
        return this.durationDays;
    }

    public ModerationAction durationDays(Duration durationDays) {
        this.setDurationDays(durationDays);
        return this;
    }

    public void setDurationDays(Duration durationDays) {
        this.durationDays = durationDays;
    }

    public Long getTicketId() {
        return this.ticketId;
    }

    public ModerationAction ticketId(Long ticketId) {
        this.setTicketId(ticketId);
        return this;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModerationAction)) {
            return false;
        }
        return getId() != null && getId().equals(((ModerationAction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModerationAction{" +
            "id=" + getId() +
            ", actionType='" + getActionType() + "'" +
            ", reason='" + getReason() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", durationDays='" + getDurationDays() + "'" +
            ", ticketId=" + getTicketId() +
            "}";
    }
}
