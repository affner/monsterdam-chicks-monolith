package com.monsterdam.app.service.dto.bff;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ContentSetDTO {

    private Long id;
    private String title;
    private String description;
    private boolean unlocked;
    private boolean canUnlock;
    private BigDecimal price;
    private String currency;
    private String downloadUrl;
    private List<MediaItemDTO> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isCanUnlock() {
        return canUnlock;
    }

    public void setCanUnlock(boolean canUnlock) {
        this.canUnlock = canUnlock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public List<MediaItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MediaItemDTO> items) {
        this.items = items;
    }
}
