package com.monsterdam.app.service.dto.bff;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for the public model profile screen.
 */
public class ModelProfileDto {

    private Long id;
    private String nickName;
    private String fullName;
    private String handle;
    private String biography;
    private boolean isFree;
    private String profilePhotoUrl;
    private String coverPhotoUrl;
    private String thumbnailUrl;
    private Long followersCount;
    private List<String> links = new ArrayList<>();
    private List<ModelSetPreviewDto> sets = new ArrayList<>();

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

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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

    public void setIsFree(boolean free) {
        isFree = free;
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

    public Long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Long followersCount) {
        this.followersCount = followersCount;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<ModelSetPreviewDto> getSets() {
        return sets;
    }

    public void setSets(List<ModelSetPreviewDto> sets) {
        this.sets = sets;
    }
}
