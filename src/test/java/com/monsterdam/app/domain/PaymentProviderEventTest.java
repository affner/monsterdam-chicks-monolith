package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PaymentProviderEventTestSamples.*;
import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentProviderEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentProviderEvent.class);
        PaymentProviderEvent paymentProviderEvent1 = getPaymentProviderEventSample1();
        PaymentProviderEvent paymentProviderEvent2 = new PaymentProviderEvent();
        assertThat(paymentProviderEvent1).isNotEqualTo(paymentProviderEvent2);

        paymentProviderEvent2.setId(paymentProviderEvent1.getId());
        assertThat(paymentProviderEvent1).isEqualTo(paymentProviderEvent2);

        paymentProviderEvent2 = getPaymentProviderEventSample2();
        assertThat(paymentProviderEvent1).isNotEqualTo(paymentProviderEvent2);
    }

    @Test
    void paymentTest() {
        PaymentProviderEvent paymentProviderEvent = getPaymentProviderEventRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        paymentProviderEvent.setPayment(paymentBack);
        assertThat(paymentProviderEvent.getPayment()).isEqualTo(paymentBack);

        paymentProviderEvent.payment(null);
        assertThat(paymentProviderEvent.getPayment()).isNull();
    }
}
