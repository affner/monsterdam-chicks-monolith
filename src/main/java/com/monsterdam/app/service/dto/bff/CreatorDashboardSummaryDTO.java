package com.monsterdam.app.service.dto.bff;

import java.math.BigDecimal;

public class CreatorDashboardSummaryDTO {

    private final long posts;
    private final long videos;
    private final long photos;
    private final long audios;
    private final BigDecimal totalEarnings;

    public CreatorDashboardSummaryDTO(long posts, long videos, long photos, long audios, BigDecimal totalEarnings) {
        this.posts = posts;
        this.videos = videos;
        this.photos = photos;
        this.audios = audios;
        this.totalEarnings = totalEarnings;
    }

    public long getPosts() {
        return posts;
    }

    public long getVideos() {
        return videos;
    }

    public long getPhotos() {
        return photos;
    }

    public long getAudios() {
        return audios;
    }

    public BigDecimal getTotalEarnings() {
        return totalEarnings;
    }
}
