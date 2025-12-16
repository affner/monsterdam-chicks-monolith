package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A PersonalSocialLinks.
 */
@Entity
@Table(name = "personal_social_links")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalSocialLinks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "normal_image_s_3_key")
    private String normalImageS3Key;

    @Column(name = "thumbnail_icon_s_3_key")
    private String thumbnailIconS3Key;

    @NotNull
    @Column(name = "social_link", nullable = false, unique = true)
    private String socialLink;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private SocialNetwork socialNetwork;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "stateOfResidence", "user" }, allowSetters = true)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonalSocialLinks id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNormalImageS3Key() {
        return this.normalImageS3Key;
    }

    public PersonalSocialLinks normalImageS3Key(String normalImageS3Key) {
        this.setNormalImageS3Key(normalImageS3Key);
        return this;
    }

    public void setNormalImageS3Key(String normalImageS3Key) {
        this.normalImageS3Key = normalImageS3Key;
    }

    public String getThumbnailIconS3Key() {
        return this.thumbnailIconS3Key;
    }

    public PersonalSocialLinks thumbnailIconS3Key(String thumbnailIconS3Key) {
        this.setThumbnailIconS3Key(thumbnailIconS3Key);
        return this;
    }

    public void setThumbnailIconS3Key(String thumbnailIconS3Key) {
        this.thumbnailIconS3Key = thumbnailIconS3Key;
    }

    public String getSocialLink() {
        return this.socialLink;
    }

    public PersonalSocialLinks socialLink(String socialLink) {
        this.setSocialLink(socialLink);
        return this;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PersonalSocialLinks createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PersonalSocialLinks lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PersonalSocialLinks createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PersonalSocialLinks lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public PersonalSocialLinks deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public SocialNetwork getSocialNetwork() {
        return this.socialNetwork;
    }

    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public PersonalSocialLinks socialNetwork(SocialNetwork socialNetwork) {
        this.setSocialNetwork(socialNetwork);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public PersonalSocialLinks userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalSocialLinks)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonalSocialLinks) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalSocialLinks{" +
            "id=" + getId() +
            ", normalImageS3Key='" + getNormalImageS3Key() + "'" +
            ", thumbnailIconS3Key='" + getThumbnailIconS3Key() + "'" +
            ", socialLink='" + getSocialLink() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
