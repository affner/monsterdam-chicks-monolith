package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.UserGender;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.monsterdam.app.domain.UserLite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserLiteDTO implements Serializable {

    private Long id;

    private String thumbnailS3Key;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private UserGender gender;

    @NotNull
    private Instant createdDate;

    private Instant lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Instant deletedDate;

    @NotNull
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    private String nickName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$")
    private String fullName;

    private UserProfileDTO profile;

    private UserSettingsDTO settings;

    private CountryDTO countryOfBirth;

    private Set<ChatRoomDTO> chatRooms = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbnailS3Key() {
        return thumbnailS3Key;
    }

    public void setThumbnailS3Key(String thumbnailS3Key) {
        this.thumbnailS3Key = thumbnailS3Key;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(UserProfileDTO profile) {
        this.profile = profile;
    }

    public UserSettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(UserSettingsDTO settings) {
        this.settings = settings;
    }

    public CountryDTO getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(CountryDTO countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public Set<ChatRoomDTO> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(Set<ChatRoomDTO> chatRooms) {
        this.chatRooms = chatRooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserLiteDTO)) {
            return false;
        }

        UserLiteDTO userLiteDTO = (UserLiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userLiteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserLiteDTO{" +
            "id=" + getId() +
            ", thumbnailS3Key='" + getThumbnailS3Key() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", gender='" + getGender() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", nickName='" + getNickName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", profile=" + getProfile() +
            ", settings=" + getSettings() +
            ", countryOfBirth=" + getCountryOfBirth() +
            ", chatRooms=" + getChatRooms() +
            "}";
    }
}
