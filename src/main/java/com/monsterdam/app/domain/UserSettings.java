package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.UserLanguage;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A UserSettings.
 */
@Entity
@Table(name = "user_settings")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "dark_mode", nullable = false)
    private Boolean darkMode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private UserLanguage language;

    @NotNull
    @Column(name = "content_filter", nullable = false)
    private Boolean contentFilter;

    @Column(name = "message_blur_intensity")
    private Integer messageBlurIntensity;

    @NotNull
    @Column(name = "activity_status_visibility", nullable = false)
    private Boolean activityStatusVisibility;

    @NotNull
    @Column(name = "two_factor_authentication", nullable = false)
    private Boolean twoFactorAuthentication;

    @Column(name = "sessions_active_count")
    private Integer sessionsActiveCount;

    @NotNull
    @Column(name = "email_notifications", nullable = false)
    private Boolean emailNotifications;

    @NotNull
    @Column(name = "important_subscription_notifications", nullable = false)
    private Boolean importantSubscriptionNotifications;

    @NotNull
    @Column(name = "new_messages", nullable = false)
    private Boolean newMessages;

    @NotNull
    @Column(name = "post_replies", nullable = false)
    private Boolean postReplies;

    @NotNull
    @Column(name = "post_likes", nullable = false)
    private Boolean postLikes;

    @NotNull
    @Column(name = "new_followers", nullable = false)
    private Boolean newFollowers;

    @NotNull
    @Column(name = "sms_new_stream", nullable = false)
    private Boolean smsNewStream;

    @NotNull
    @Column(name = "toast_new_comment", nullable = false)
    private Boolean toastNewComment;

    @NotNull
    @Column(name = "toast_new_likes", nullable = false)
    private Boolean toastNewLikes;

    @NotNull
    @Column(name = "toast_new_stream", nullable = false)
    private Boolean toastNewStream;

    @NotNull
    @Column(name = "site_new_comment", nullable = false)
    private Boolean siteNewComment;

    @NotNull
    @Column(name = "site_new_likes", nullable = false)
    private Boolean siteNewLikes;

    @NotNull
    @Column(name = "site_discounts_from_followed_users", nullable = false)
    private Boolean siteDiscountsFromFollowedUsers;

    @NotNull
    @Column(name = "site_new_stream", nullable = false)
    private Boolean siteNewStream;

    @NotNull
    @Column(name = "site_upcoming_stream_reminders", nullable = false)
    private Boolean siteUpcomingStreamReminders;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @JsonIgnoreProperties(value = { "profile", "settings", "countryOfBirth" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "settings")
    private UserLite user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSettings id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDarkMode() {
        return this.darkMode;
    }

    public UserSettings darkMode(Boolean darkMode) {
        this.setDarkMode(darkMode);
        return this;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
    }

    public UserLanguage getLanguage() {
        return this.language;
    }

    public UserSettings language(UserLanguage language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(UserLanguage language) {
        this.language = language;
    }

    public Boolean getContentFilter() {
        return this.contentFilter;
    }

    public UserSettings contentFilter(Boolean contentFilter) {
        this.setContentFilter(contentFilter);
        return this;
    }

    public void setContentFilter(Boolean contentFilter) {
        this.contentFilter = contentFilter;
    }

    public Integer getMessageBlurIntensity() {
        return this.messageBlurIntensity;
    }

    public UserSettings messageBlurIntensity(Integer messageBlurIntensity) {
        this.setMessageBlurIntensity(messageBlurIntensity);
        return this;
    }

    public void setMessageBlurIntensity(Integer messageBlurIntensity) {
        this.messageBlurIntensity = messageBlurIntensity;
    }

    public Boolean getActivityStatusVisibility() {
        return this.activityStatusVisibility;
    }

    public UserSettings activityStatusVisibility(Boolean activityStatusVisibility) {
        this.setActivityStatusVisibility(activityStatusVisibility);
        return this;
    }

    public void setActivityStatusVisibility(Boolean activityStatusVisibility) {
        this.activityStatusVisibility = activityStatusVisibility;
    }

    public Boolean getTwoFactorAuthentication() {
        return this.twoFactorAuthentication;
    }

    public UserSettings twoFactorAuthentication(Boolean twoFactorAuthentication) {
        this.setTwoFactorAuthentication(twoFactorAuthentication);
        return this;
    }

    public void setTwoFactorAuthentication(Boolean twoFactorAuthentication) {
        this.twoFactorAuthentication = twoFactorAuthentication;
    }

    public Integer getSessionsActiveCount() {
        return this.sessionsActiveCount;
    }

    public UserSettings sessionsActiveCount(Integer sessionsActiveCount) {
        this.setSessionsActiveCount(sessionsActiveCount);
        return this;
    }

    public void setSessionsActiveCount(Integer sessionsActiveCount) {
        this.sessionsActiveCount = sessionsActiveCount;
    }

    public Boolean getEmailNotifications() {
        return this.emailNotifications;
    }

    public UserSettings emailNotifications(Boolean emailNotifications) {
        this.setEmailNotifications(emailNotifications);
        return this;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getImportantSubscriptionNotifications() {
        return this.importantSubscriptionNotifications;
    }

    public UserSettings importantSubscriptionNotifications(Boolean importantSubscriptionNotifications) {
        this.setImportantSubscriptionNotifications(importantSubscriptionNotifications);
        return this;
    }

    public void setImportantSubscriptionNotifications(Boolean importantSubscriptionNotifications) {
        this.importantSubscriptionNotifications = importantSubscriptionNotifications;
    }

    public Boolean getNewMessages() {
        return this.newMessages;
    }

    public UserSettings newMessages(Boolean newMessages) {
        this.setNewMessages(newMessages);
        return this;
    }

    public void setNewMessages(Boolean newMessages) {
        this.newMessages = newMessages;
    }

    public Boolean getPostReplies() {
        return this.postReplies;
    }

    public UserSettings postReplies(Boolean postReplies) {
        this.setPostReplies(postReplies);
        return this;
    }

    public void setPostReplies(Boolean postReplies) {
        this.postReplies = postReplies;
    }

    public Boolean getPostLikes() {
        return this.postLikes;
    }

    public UserSettings postLikes(Boolean postLikes) {
        this.setPostLikes(postLikes);
        return this;
    }

    public void setPostLikes(Boolean postLikes) {
        this.postLikes = postLikes;
    }

    public Boolean getNewFollowers() {
        return this.newFollowers;
    }

    public UserSettings newFollowers(Boolean newFollowers) {
        this.setNewFollowers(newFollowers);
        return this;
    }

    public void setNewFollowers(Boolean newFollowers) {
        this.newFollowers = newFollowers;
    }

    public Boolean getSmsNewStream() {
        return this.smsNewStream;
    }

    public UserSettings smsNewStream(Boolean smsNewStream) {
        this.setSmsNewStream(smsNewStream);
        return this;
    }

    public void setSmsNewStream(Boolean smsNewStream) {
        this.smsNewStream = smsNewStream;
    }

    public Boolean getToastNewComment() {
        return this.toastNewComment;
    }

    public UserSettings toastNewComment(Boolean toastNewComment) {
        this.setToastNewComment(toastNewComment);
        return this;
    }

    public void setToastNewComment(Boolean toastNewComment) {
        this.toastNewComment = toastNewComment;
    }

    public Boolean getToastNewLikes() {
        return this.toastNewLikes;
    }

    public UserSettings toastNewLikes(Boolean toastNewLikes) {
        this.setToastNewLikes(toastNewLikes);
        return this;
    }

    public void setToastNewLikes(Boolean toastNewLikes) {
        this.toastNewLikes = toastNewLikes;
    }

    public Boolean getToastNewStream() {
        return this.toastNewStream;
    }

    public UserSettings toastNewStream(Boolean toastNewStream) {
        this.setToastNewStream(toastNewStream);
        return this;
    }

    public void setToastNewStream(Boolean toastNewStream) {
        this.toastNewStream = toastNewStream;
    }

    public Boolean getSiteNewComment() {
        return this.siteNewComment;
    }

    public UserSettings siteNewComment(Boolean siteNewComment) {
        this.setSiteNewComment(siteNewComment);
        return this;
    }

    public void setSiteNewComment(Boolean siteNewComment) {
        this.siteNewComment = siteNewComment;
    }

    public Boolean getSiteNewLikes() {
        return this.siteNewLikes;
    }

    public UserSettings siteNewLikes(Boolean siteNewLikes) {
        this.setSiteNewLikes(siteNewLikes);
        return this;
    }

    public void setSiteNewLikes(Boolean siteNewLikes) {
        this.siteNewLikes = siteNewLikes;
    }

    public Boolean getSiteDiscountsFromFollowedUsers() {
        return this.siteDiscountsFromFollowedUsers;
    }

    public UserSettings siteDiscountsFromFollowedUsers(Boolean siteDiscountsFromFollowedUsers) {
        this.setSiteDiscountsFromFollowedUsers(siteDiscountsFromFollowedUsers);
        return this;
    }

    public void setSiteDiscountsFromFollowedUsers(Boolean siteDiscountsFromFollowedUsers) {
        this.siteDiscountsFromFollowedUsers = siteDiscountsFromFollowedUsers;
    }

    public Boolean getSiteNewStream() {
        return this.siteNewStream;
    }

    public UserSettings siteNewStream(Boolean siteNewStream) {
        this.setSiteNewStream(siteNewStream);
        return this;
    }

    public void setSiteNewStream(Boolean siteNewStream) {
        this.siteNewStream = siteNewStream;
    }

    public Boolean getSiteUpcomingStreamReminders() {
        return this.siteUpcomingStreamReminders;
    }

    public UserSettings siteUpcomingStreamReminders(Boolean siteUpcomingStreamReminders) {
        this.setSiteUpcomingStreamReminders(siteUpcomingStreamReminders);
        return this;
    }

    public void setSiteUpcomingStreamReminders(Boolean siteUpcomingStreamReminders) {
        this.siteUpcomingStreamReminders = siteUpcomingStreamReminders;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserSettings createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserSettings lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserSettings createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserSettings lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserSettings isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UserLite getUser() {
        return this.user;
    }

    public void setUser(UserLite userLite) {
        if (this.user != null) {
            this.user.setSettings(null);
        }
        if (userLite != null) {
            userLite.setSettings(this);
        }
        this.user = userLite;
    }

    public UserSettings user(UserLite userLite) {
        this.setUser(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSettings)) {
            return false;
        }
        return getId() != null && getId().equals(((UserSettings) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSettings{" +
            "id=" + getId() +
            ", darkMode='" + getDarkMode() + "'" +
            ", language='" + getLanguage() + "'" +
            ", contentFilter='" + getContentFilter() + "'" +
            ", messageBlurIntensity=" + getMessageBlurIntensity() +
            ", activityStatusVisibility='" + getActivityStatusVisibility() + "'" +
            ", twoFactorAuthentication='" + getTwoFactorAuthentication() + "'" +
            ", sessionsActiveCount=" + getSessionsActiveCount() +
            ", emailNotifications='" + getEmailNotifications() + "'" +
            ", importantSubscriptionNotifications='" + getImportantSubscriptionNotifications() + "'" +
            ", newMessages='" + getNewMessages() + "'" +
            ", postReplies='" + getPostReplies() + "'" +
            ", postLikes='" + getPostLikes() + "'" +
            ", newFollowers='" + getNewFollowers() + "'" +
            ", smsNewStream='" + getSmsNewStream() + "'" +
            ", toastNewComment='" + getToastNewComment() + "'" +
            ", toastNewLikes='" + getToastNewLikes() + "'" +
            ", toastNewStream='" + getToastNewStream() + "'" +
            ", siteNewComment='" + getSiteNewComment() + "'" +
            ", siteNewLikes='" + getSiteNewLikes() + "'" +
            ", siteDiscountsFromFollowedUsers='" + getSiteDiscountsFromFollowedUsers() + "'" +
            ", siteNewStream='" + getSiteNewStream() + "'" +
            ", siteUpcomingStreamReminders='" + getSiteUpcomingStreamReminders() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
