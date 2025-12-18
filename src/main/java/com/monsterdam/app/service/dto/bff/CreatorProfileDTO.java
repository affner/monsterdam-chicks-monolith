package com.monsterdam.app.service.dto.bff;

import java.util.ArrayList;
import java.util.List;

public class CreatorProfileDTO {

    private Long creatorId;
    private String displayName;
    private String bioShort;
    private String avatarUrl;
    private String coverUrl;
    private List<String> links = new ArrayList<>();
    private List<SingleSetDTO> sets = new ArrayList<>();

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBioShort() {
        return bioShort;
    }

    public void setBioShort(String bioShort) {
        this.bioShort = bioShort;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<SingleSetDTO> getSets() {
        return sets;
    }

    public void setSets(List<SingleSetDTO> sets) {
        this.sets = sets;
    }
}
