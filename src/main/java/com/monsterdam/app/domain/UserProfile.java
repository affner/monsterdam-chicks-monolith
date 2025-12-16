package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A UserProfile.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "email_contact", nullable = false)
    private String emailContact;

    @Column(name = "profile_photo_s_3_key")
    private String profilePhotoS3Key;

    @Column(name = "cover_photo_s_3_key")
    private String coverPhotoS3Key;

    @Column(name = "main_content_url")
    private String mainContentUrl;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "amazon_wishlist_url")
    private String amazonWishlistUrl;

    @NotNull
    @Column(name = "last_login_date", nullable = false)
    private Instant lastLoginDate;

    @Lob
    @Column(name = "biography")
    private String biography;

    @Column(name = "is_free")
    private Boolean isFree;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private State stateOfResidence;

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
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "profile")
    private UserLite user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailContact() {
        return this.emailContact;
    }

    public UserProfile emailContact(String emailContact) {
        this.setEmailContact(emailContact);
        return this;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getProfilePhotoS3Key() {
        return this.profilePhotoS3Key;
    }

    public UserProfile profilePhotoS3Key(String profilePhotoS3Key) {
        this.setProfilePhotoS3Key(profilePhotoS3Key);
        return this;
    }

    public void setProfilePhotoS3Key(String profilePhotoS3Key) {
        this.profilePhotoS3Key = profilePhotoS3Key;
    }

    public String getCoverPhotoS3Key() {
        return this.coverPhotoS3Key;
    }

    public UserProfile coverPhotoS3Key(String coverPhotoS3Key) {
        this.setCoverPhotoS3Key(coverPhotoS3Key);
        return this;
    }

    public void setCoverPhotoS3Key(String coverPhotoS3Key) {
        this.coverPhotoS3Key = coverPhotoS3Key;
    }

    public String getMainContentUrl() {
        return this.mainContentUrl;
    }

    public UserProfile mainContentUrl(String mainContentUrl) {
        this.setMainContentUrl(mainContentUrl);
        return this;
    }

    public void setMainContentUrl(String mainContentUrl) {
        this.mainContentUrl = mainContentUrl;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public UserProfile mobilePhone(String mobilePhone) {
        this.setMobilePhone(mobilePhone);
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    public UserProfile websiteUrl(String websiteUrl) {
        this.setWebsiteUrl(websiteUrl);
        return this;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAmazonWishlistUrl() {
        return this.amazonWishlistUrl;
    }

    public UserProfile amazonWishlistUrl(String amazonWishlistUrl) {
        this.setAmazonWishlistUrl(amazonWishlistUrl);
        return this;
    }

    public void setAmazonWishlistUrl(String amazonWishlistUrl) {
        this.amazonWishlistUrl = amazonWishlistUrl;
    }

    public Instant getLastLoginDate() {
        return this.lastLoginDate;
    }

    public UserProfile lastLoginDate(Instant lastLoginDate) {
        this.setLastLoginDate(lastLoginDate);
        return this;
    }

    public void setLastLoginDate(Instant lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getBiography() {
        return this.biography;
    }

    public UserProfile biography(String biography) {
        this.setBiography(biography);
        return this;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Boolean getIsFree() {
        return this.isFree;
    }

    public UserProfile isFree(Boolean isFree) {
        this.setIsFree(isFree);
        return this;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public UserProfile createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public UserProfile lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public UserProfile createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public UserProfile lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public UserProfile deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public State getStateOfResidence() {
        return this.stateOfResidence;
    }

    public void setStateOfResidence(State state) {
        this.stateOfResidence = state;
    }

    public UserProfile stateOfResidence(State state) {
        this.setStateOfResidence(state);
        return this;
    }

    public UserLite getUser() {
        return this.user;
    }

    public void setUser(UserLite userLite) {
        if (this.user != null) {
            this.user.setProfile(null);
        }
        if (userLite != null) {
            userLite.setProfile(this);
        }
        this.user = userLite;
    }

    public UserProfile user(UserLite userLite) {
        this.setUser(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", emailContact='" + getEmailContact() + "'" +
            ", profilePhotoS3Key='" + getProfilePhotoS3Key() + "'" +
            ", coverPhotoS3Key='" + getCoverPhotoS3Key() + "'" +
            ", mainContentUrl='" + getMainContentUrl() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", websiteUrl='" + getWebsiteUrl() + "'" +
            ", amazonWishlistUrl='" + getAmazonWishlistUrl() + "'" +
            ", lastLoginDate='" + getLastLoginDate() + "'" +
            ", biography='" + getBiography() + "'" +
            ", isFree='" + getIsFree() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
