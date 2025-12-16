package com.monsterdam.app.service.dto;

import com.monsterdam.app.domain.enumeration.GenericStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.monsterdam.app.domain.Payment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Size(max = 3)
    private String currency;

    @NotNull
    private Instant paymentDate;

    @NotNull
    private GenericStatus paymentStatus;

    @Size(max = 100)
    private String paymentReference;

    @Size(max = 100)
    private String cloudTransactionId;

    @Size(max = 100)
    private String providerPaymentIntentId;

    @Size(max = 100)
    private String providerChargeId;

    @Size(max = 100)
    private String providerCustomerId;

    @Size(max = 100)
    private String providerPaymentMethodId;

    @Size(max = 100)
    private String providerEventLastId;

    @Size(max = 2)
    private String countryCode;

    private BigDecimal providerFeeAmount;

    @NotNull
    private BigDecimal platformFeeAmount;

    @NotNull
    private BigDecimal creatorNetAmount;

    private BigDecimal taxAmount;

    private Instant authorizedDate;

    private Instant capturedDate;

    private PaymentMethodDTO method;

    private PaymentProviderDTO provider;

    @NotNull
    private UserLiteDTO viewer;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public GenericStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(GenericStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getCloudTransactionId() {
        return cloudTransactionId;
    }

    public void setCloudTransactionId(String cloudTransactionId) {
        this.cloudTransactionId = cloudTransactionId;
    }

    public String getProviderPaymentIntentId() {
        return providerPaymentIntentId;
    }

    public void setProviderPaymentIntentId(String providerPaymentIntentId) {
        this.providerPaymentIntentId = providerPaymentIntentId;
    }

    public String getProviderChargeId() {
        return providerChargeId;
    }

    public void setProviderChargeId(String providerChargeId) {
        this.providerChargeId = providerChargeId;
    }

    public String getProviderCustomerId() {
        return providerCustomerId;
    }

    public void setProviderCustomerId(String providerCustomerId) {
        this.providerCustomerId = providerCustomerId;
    }

    public String getProviderPaymentMethodId() {
        return providerPaymentMethodId;
    }

    public void setProviderPaymentMethodId(String providerPaymentMethodId) {
        this.providerPaymentMethodId = providerPaymentMethodId;
    }

    public String getProviderEventLastId() {
        return providerEventLastId;
    }

    public void setProviderEventLastId(String providerEventLastId) {
        this.providerEventLastId = providerEventLastId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getProviderFeeAmount() {
        return providerFeeAmount;
    }

    public void setProviderFeeAmount(BigDecimal providerFeeAmount) {
        this.providerFeeAmount = providerFeeAmount;
    }

    public BigDecimal getPlatformFeeAmount() {
        return platformFeeAmount;
    }

    public void setPlatformFeeAmount(BigDecimal platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public BigDecimal getCreatorNetAmount() {
        return creatorNetAmount;
    }

    public void setCreatorNetAmount(BigDecimal creatorNetAmount) {
        this.creatorNetAmount = creatorNetAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Instant getAuthorizedDate() {
        return authorizedDate;
    }

    public void setAuthorizedDate(Instant authorizedDate) {
        this.authorizedDate = authorizedDate;
    }

    public Instant getCapturedDate() {
        return capturedDate;
    }

    public void setCapturedDate(Instant capturedDate) {
        this.capturedDate = capturedDate;
    }

    public PaymentMethodDTO getMethod() {
        return method;
    }

    public void setMethod(PaymentMethodDTO method) {
        this.method = method;
    }

    public PaymentProviderDTO getProvider() {
        return provider;
    }

    public void setProvider(PaymentProviderDTO provider) {
        this.provider = provider;
    }

    public UserLiteDTO getViewer() {
        return viewer;
    }

    public void setViewer(UserLiteDTO viewer) {
        this.viewer = viewer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
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
            ", method=" + getMethod() +
            ", provider=" + getProvider() +
            ", viewer=" + getViewer() +
            "}";
    }
}
