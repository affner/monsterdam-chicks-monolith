package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static com.monsterdam.app.domain.RefundTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefundTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RefundTransaction.class);
        RefundTransaction refundTransaction1 = getRefundTransactionSample1();
        RefundTransaction refundTransaction2 = new RefundTransaction();
        assertThat(refundTransaction1).isNotEqualTo(refundTransaction2);

        refundTransaction2.setId(refundTransaction1.getId());
        assertThat(refundTransaction1).isEqualTo(refundTransaction2);

        refundTransaction2 = getRefundTransactionSample2();
        assertThat(refundTransaction1).isNotEqualTo(refundTransaction2);
    }

    @Test
    void paymentTest() {
        RefundTransaction refundTransaction = getRefundTransactionRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        refundTransaction.setPayment(paymentBack);
        assertThat(refundTransaction.getPayment()).isEqualTo(paymentBack);

        refundTransaction.payment(null);
        assertThat(refundTransaction.getPayment()).isNull();
    }
}
