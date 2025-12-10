package com.monsterdam.app.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.HelpQuestion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HelpQuestionDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private String content;

    @NotNull
    private Boolean isDeleted;

    private HelpSubcategoryDTO subcategory;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public HelpSubcategoryDTO getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(HelpSubcategoryDTO subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HelpQuestionDTO)) {
            return false;
        }

        HelpQuestionDTO helpQuestionDTO = (HelpQuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, helpQuestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HelpQuestionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", subcategory=" + getSubcategory() +
            "}";
    }
}
