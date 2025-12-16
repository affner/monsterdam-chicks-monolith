package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A Country.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "country")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "alpha_2_code", length = 2, nullable = false)
    private String alpha2Code;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "alpha_3_code", length = 3, nullable = false)
    private String alpha3Code;

    @Column(name = "phone_code")
    private String phoneCode;

    @Lob
    @Column(name = "thumbnail_country")
    private byte[] thumbnailCountry;

    @Column(name = "thumbnail_country_content_type")
    private String thumbnailCountryContentType;

    @NotNull
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "deleted_date")
    private Instant deletedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private Set<State> states = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha2Code() {
        return this.alpha2Code;
    }

    public Country alpha2Code(String alpha2Code) {
        this.setAlpha2Code(alpha2Code);
        return this;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getAlpha3Code() {
        return this.alpha3Code;
    }

    public Country alpha3Code(String alpha3Code) {
        this.setAlpha3Code(alpha3Code);
        return this;
    }

    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    public String getPhoneCode() {
        return this.phoneCode;
    }

    public Country phoneCode(String phoneCode) {
        this.setPhoneCode(phoneCode);
        return this;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public byte[] getThumbnailCountry() {
        return this.thumbnailCountry;
    }

    public Country thumbnailCountry(byte[] thumbnailCountry) {
        this.setThumbnailCountry(thumbnailCountry);
        return this;
    }

    public void setThumbnailCountry(byte[] thumbnailCountry) {
        this.thumbnailCountry = thumbnailCountry;
    }

    public String getThumbnailCountryContentType() {
        return this.thumbnailCountryContentType;
    }

    public Country thumbnailCountryContentType(String thumbnailCountryContentType) {
        this.thumbnailCountryContentType = thumbnailCountryContentType;
        return this;
    }

    public void setThumbnailCountryContentType(String thumbnailCountryContentType) {
        this.thumbnailCountryContentType = thumbnailCountryContentType;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Country createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Country lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Country createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Country lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getDeletedDate() {
        return this.deletedDate;
    }

    public Country deletedDate(Instant deletedDate) {
        this.setDeletedDate(deletedDate);
        return this;
    }

    public void setDeletedDate(Instant deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Set<State> getStates() {
        return this.states;
    }

    public void setStates(Set<State> states) {
        if (this.states != null) {
            this.states.forEach(i -> i.setCountry(null));
        }
        if (states != null) {
            states.forEach(i -> i.setCountry(this));
        }
        this.states = states;
    }

    public Country states(Set<State> states) {
        this.setStates(states);
        return this;
    }

    public Country addStates(State state) {
        this.states.add(state);
        state.setCountry(this);
        return this;
    }

    public Country removeStates(State state) {
        this.states.remove(state);
        state.setCountry(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return getId() != null && getId().equals(((Country) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alpha2Code='" + getAlpha2Code() + "'" +
            ", alpha3Code='" + getAlpha3Code() + "'" +
            ", phoneCode='" + getPhoneCode() + "'" +
            ", thumbnailCountry='" + getThumbnailCountry() + "'" +
            ", thumbnailCountryContentType='" + getThumbnailCountryContentType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", deletedDate='" + getDeletedDate() + "'" +
            "}";
    }
}
