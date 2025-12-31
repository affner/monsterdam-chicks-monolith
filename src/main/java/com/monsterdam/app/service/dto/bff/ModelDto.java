package com.monsterdam.app.service.dto.bff;

import jakarta.persistence.Lob;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO representing a creator for browsing purposes.  It aggregates basic
 * profile information along with precomputed URLs for images and a list of
 * packages.
 */
public class ModelDto {

    private Long id;
    private String nickName;
    private String fullName;
    private String biography;
    private boolean isFree;
    private String profilePhotoUrl;
    private String coverPhotoUrl;
    private String thumbnailUrl;

    @Lob
    private byte[] thumbnail;

    private List<PackageDto> packages = new ArrayList<>();

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setIsFree(boolean aFree) {
        isFree = aFree;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        this.coverPhotoUrl = coverPhotoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<PackageDto> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDto> packages) {
        this.packages = packages;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
}
