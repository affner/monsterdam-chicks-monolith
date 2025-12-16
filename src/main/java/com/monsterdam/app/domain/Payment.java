package com.monsterdam.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.monsterdam.app.domain.enumeration.GenericStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Size(max = 3)
    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private GenericStatus paymentStatus;

    @Size(max = 100)
    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Size(max = 100)
    @Column(name = "cloud_transaction_id", length = 100)
    private String cloudTransactionId;

    @Size(max = 100)
    @Column(name = "provider_payment_intent_id", length = 100)
    private String providerPaymentIntentId;

    @Size(max = 100)
    @Column(name = "provider_charge_id", length = 100)
    private String providerChargeId;

    @Size(max = 100)
    @Column(name = "provider_customer_id", length = 100)
    private String providerCustomerId;

    @Size(max = 100)
    @Column(name = "provider_payment_method_id", length = 100)
    private String providerPaymentMethodId;

    @Size(max = 100)
    @Column(name = "provider_event_last_id", length = 100)
    private String providerEventLastId;

    @Size(max = 2)
    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "provider_fee_amount", precision = 21, scale = 2)
    private BigDecimal providerFeeAmount;

    @NotNull
    @Column(name = "platform_fee_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal platformFeeAmount;

    @NotNull
    @Column(name = "creator_net_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal creatorNetAmount;

    @Column(name = "tax_amount", precision = 21, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "authorized_date")
    private Instant authorizedDate;

    @Column(name = "captured_date")
    private Instant capturedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethod method;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentProvider provider;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "profile",
            "settings",
            "countryOfBirth",
            "purchasedSubscriptionsAsViewers",
            "purchasedSubscriptionsAsCreators",
            "pollVotes",
            "associationsAsOwners",
            "assistanceTickets",
            "payments",
            "comments",
            "posts",
            "singleAudios",
            "singleVideos",
            "singlePhotos",
            "videoStories",
            "directMessagesSents",
            "likeMarks",
            "notificationsReceiveds",
            "notificationsTriggereds",
            "postMentions",
            "commentMentions",
            "chatRooms",
        },
        allowSetters = true
    )
    private UserLite viewer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Payment currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public Payment paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public GenericStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public Payment paymentStatus(GenericStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(GenericStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentReference() {
        return this.paymentReference;
    }

    public Payment paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getCloudTransactionId() {
        return this.cloudTransactionId;
    }

    public Payment cloudTransactionId(String cloudTransactionId) {
        this.setCloudTransactionId(cloudTransactionId);
        return this;
    }

    public void setCloudTransactionId(String cloudTransactionId) {
        this.cloudTransactionId = cloudTransactionId;
    }

    public String getProviderPaymentIntentId() {
        return this.providerPaymentIntentId;
    }

    public Payment providerPaymentIntentId(String providerPaymentIntentId) {
        this.setProviderPaymentIntentId(providerPaymentIntentId);
        return this;
    }

    public void setProviderPaymentIntentId(String providerPaymentIntentId) {
        this.providerPaymentIntentId = providerPaymentIntentId;
    }

    public String getProviderChargeId() {
        return this.providerChargeId;
    }

    public Payment providerChargeId(String providerChargeId) {
        this.setProviderChargeId(providerChargeId);
        return this;
    }

    public void setProviderChargeId(String providerChargeId) {
        this.providerChargeId = providerChargeId;
    }

    public String getProviderCustomerId() {
        return this.providerCustomerId;
    }

    public Payment providerCustomerId(String providerCustomerId) {
        this.setProviderCustomerId(providerCustomerId);
        return this;
    }

    public void setProviderCustomerId(String providerCustomerId) {
        this.providerCustomerId = providerCustomerId;
    }

    public String getProviderPaymentMethodId() {
        return this.providerPaymentMethodId;
    }

    public Payment providerPaymentMethodId(String providerPaymentMethodId) {
        this.setProviderPaymentMethodId(providerPaymentMethodId);
        return this;
    }

    public void setProviderPaymentMethodId(String providerPaymentMethodId) {
        this.providerPaymentMethodId = providerPaymentMethodId;
    }

    public String getProviderEventLastId() {
        return this.providerEventLastId;
    }

    public Payment providerEventLastId(String providerEventLastId) {
        this.setProviderEventLastId(providerEventLastId);
        return this;
    }

    public void setProviderEventLastId(String providerEventLastId) {
        this.providerEventLastId = providerEventLastId;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public Payment countryCode(String countryCode) {
        this.setCountryCode(countryCode);
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getProviderFeeAmount() {
        return this.providerFeeAmount;
    }

    public Payment providerFeeAmount(BigDecimal providerFeeAmount) {
        this.setProviderFeeAmount(providerFeeAmount);
        return this;
    }

    public void setProviderFeeAmount(BigDecimal providerFeeAmount) {
        this.providerFeeAmount = providerFeeAmount;
    }

    public BigDecimal getPlatformFeeAmount() {
        return this.platformFeeAmount;
    }

    public Payment platformFeeAmount(BigDecimal platformFeeAmount) {
        this.setPlatformFeeAmount(platformFeeAmount);
        return this;
    }

    public void setPlatformFeeAmount(BigDecimal platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public BigDecimal getCreatorNetAmount() {
        return this.creatorNetAmount;
    }

    public Payment creatorNetAmount(BigDecimal creatorNetAmount) {
        this.setCreatorNetAmount(creatorNetAmount);
        return this;
    }

    public void setCreatorNetAmount(BigDecimal creatorNetAmount) {
        this.creatorNetAmount = creatorNetAmount;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public Payment taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Instant getAuthorizedDate() {
        return this.authorizedDate;
    }

    public Payment authorizedDate(Instant authorizedDate) {
        this.setAuthorizedDate(authorizedDate);
        return this;
    }

    public void setAuthorizedDate(Instant authorizedDate) {
        this.authorizedDate = authorizedDate;
    }

    public Instant getCapturedDate() {
        return this.capturedDate;
    }

    public Payment capturedDate(Instant capturedDate) {
        this.setCapturedDate(capturedDate);
        return this;
    }

    public void setCapturedDate(Instant capturedDate) {
        this.capturedDate = capturedDate;
    }

    public PaymentMethod getMethod() {
        return this.method;
    }

    public void setMethod(PaymentMethod paymentMethod) {
        this.method = paymentMethod;
    }

    public Payment method(PaymentMethod paymentMethod) {
        this.setMethod(paymentMethod);
        return this;
    }

    public PaymentProvider getProvider() {
        return this.provider;
    }

    public void setProvider(PaymentProvider paymentProvider) {
        this.provider = paymentProvider;
    }

    public Payment provider(PaymentProvider paymentProvider) {
        this.setProvider(paymentProvider);
        return this;
    }

    public UserLite getViewer() {
        return this.viewer;
    }

    public void setViewer(UserLite userLite) {
        this.viewer = userLite;
    }

    public Payment viewer(UserLite userLite) {
        this.setViewer(userLite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentReference='" + getPaymentReference() + "'" +
            ", cloudTransactionId='" + getCloudTransactionId() + "'" +
            ", providerPaymentIntentId='" + getProviderPaymentIntentId() + "'" +
            ", providerChargeId='" + getProviderChargeId() + "'" +
            ", providerCustomerId='" + getProviderCustomerId() + "'" +
            ", providerPaymentMethodId='" + getProviderPaymentMethodId() + "'" +
            ", providerEventLastId='" + getProviderEventLastId() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", providerFeeAmount=" + getProviderFeeAmount() +
            ", platformFeeAmount=" + getPlatformFeeAmount() +
            ", creatorNetAmount=" + getCreatorNetAmount() +
            ", taxAmount=" + getTaxAmount() +
            ", authorizedDate='" + getAuthorizedDate() + "'" +
            ", capturedDate='" + getCapturedDate() + "'" +
            "}";
    }
}
