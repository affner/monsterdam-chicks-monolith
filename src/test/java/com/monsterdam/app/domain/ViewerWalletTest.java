package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.PaymentTestSamples.*;
import static com.monsterdam.app.domain.ViewerWalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViewerWalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewerWallet.class);
        ViewerWallet viewerWallet1 = getViewerWalletSample1();
        ViewerWallet viewerWallet2 = new ViewerWallet();
        assertThat(viewerWallet1).isNotEqualTo(viewerWallet2);

        viewerWallet2.setId(viewerWallet1.getId());
        assertThat(viewerWallet1).isEqualTo(viewerWallet2);

        viewerWallet2 = getViewerWalletSample2();
        assertThat(viewerWallet1).isNotEqualTo(viewerWallet2);
    }

    @Test
    void paymentTest() {
        ViewerWallet viewerWallet = getViewerWalletRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        viewerWallet.setPayment(paymentBack);
        assertThat(viewerWallet.getPayment()).isEqualTo(paymentBack);

        viewerWallet.payment(null);
        assertThat(viewerWallet.getPayment()).isNull();
    }
}
