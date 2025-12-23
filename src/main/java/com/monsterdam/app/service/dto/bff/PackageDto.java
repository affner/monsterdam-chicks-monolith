package com.monsterdam.app.service.dto.bff;

import java.math.BigDecimal;

/**
 * A DTO representing a content package.  It includes only the fields
 * necessary for display: id, price, counts of images and videos, whether
 * payment is required, the owning model id and a thumbnail URL.
 */
public class PackageDto {

    private Long id;
    private BigDecimal amount;
    private Integer videoCount;
    private Integer imageCount;
    private boolean paid;
    private Long modelId;
    private String thumbnailUrl;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getImageCount() {
        return imageCount;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
