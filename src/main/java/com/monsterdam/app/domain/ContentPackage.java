package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A ContentPackage.
 */
@Entity
@Table(name = "content_package")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "video_count")
    private Integer videoCount;

    @Column(name = "image_count")
    private Integer imageCount;

    @NotNull
    @Column(name = "is_paid_content", nullable = false)
    private Boolean isPaidContent;

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

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "post_id")
    private Long postId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentPackage")
    @JsonIgnoreProperties(value = { "contentPackage", "payment", "viewer", "creator" }, allowSetters = true)
    private Set<PurchasedContent> purchases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContentPackage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public ContentPackage amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVideoCount() {
        return this.videoCount;
    }

    public ContentPackage videoCount(Integer videoCount) {
        this.setVideoCount(videoCount);
        return this;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Integer getImageCount() {
        return this.imageCount;
    }

    public ContentPackage imageCount(Integer imageCount) {
        this.setImageCount(imageCount);
        return this;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public Boolean getIsPaidContent() {
        return this.isPaidContent;
    }

    public ContentPackage isPaidContent(Boolean isPaidContent) {
        this.setIsPaidContent(isPaidContent);
        return this;
    }

    public void setIsPaidContent(Boolean isPaidContent) {
        this.isPaidContent = isPaidContent;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public ContentPackage createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ContentPackage lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ContentPackage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ContentPackage lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public ContentPackage deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public ContentPackage messageId(Long messageId) {
        this.setMessageId(messageId);
        return this;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getPostId() {
        return this.postId;
    }

    public ContentPackage postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Set<PurchasedContent> getPurchases() {
        return this.purchases;
    }

    public void setPurchases(Set<PurchasedContent> purchasedContents) {
        if (this.purchases != null) {
            this.purchases.forEach(i -> i.setContentPackage(null));
        }
        if (purchasedContents != null) {
            purchasedContents.forEach(i -> i.setContentPackage(this));
        }
        this.purchases = purchasedContents;
    }

    public ContentPackage purchases(Set<PurchasedContent> purchasedContents) {
        this.setPurchases(purchasedContents);
        return this;
    }

    public ContentPackage addPurchases(PurchasedContent purchasedContent) {
        this.purchases.add(purchasedContent);
        purchasedContent.setContentPackage(this);
        return this;
    }

    public ContentPackage removePurchases(PurchasedContent purchasedContent) {
        this.purchases.remove(purchasedContent);
        purchasedContent.setContentPackage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentPackage)) {
            return false;
        }
        return getId() != null && getId().equals(((ContentPackage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentPackage{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", videoCount=" + getVideoCount() +
            ", imageCount=" + getImageCount() +
            ", isPaidContent='" + getIsPaidContent() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            ", messageId=" + getMessageId() +
            ", postId=" + getPostId() +
            "}";
    }
}
