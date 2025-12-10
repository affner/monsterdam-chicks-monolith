package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PaymentMethodTestSamples.*;
import static com.monsterdam.app.domain.PaymentProviderTestSamples.*;
import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void methodTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentMethod paymentMethodBack = getPaymentMethodRandomSampleGenerator();

        payment.setMethod(paymentMethodBack);
        assertThat(payment.getMethod()).isEqualTo(paymentMethodBack);

        payment.method(null);
        assertThat(payment.getMethod()).isNull();
    }

    @Test
    void providerTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        PaymentProvider paymentProviderBack = getPaymentProviderRandomSampleGenerator();

        payment.setProvider(paymentProviderBack);
        assertThat(payment.getProvider()).isEqualTo(paymentProviderBack);

        payment.provider(null);
        assertThat(payment.getProvider()).isNull();
    }
}
