package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentProviderEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentProviderEventDTO.class);
        PaymentProviderEventDTO paymentProviderEventDTO1 = new PaymentProviderEventDTO();
        paymentProviderEventDTO1.setId(1L);
        PaymentProviderEventDTO paymentProviderEventDTO2 = new PaymentProviderEventDTO();
        assertThat(paymentProviderEventDTO1).isNotEqualTo(paymentProviderEventDTO2);
        paymentProviderEventDTO2.setId(paymentProviderEventDTO1.getId());
        assertThat(paymentProviderEventDTO1).isEqualTo(paymentProviderEventDTO2);
        paymentProviderEventDTO2.setId(2L);
        assertThat(paymentProviderEventDTO1).isNotEqualTo(paymentProviderEventDTO2);
        paymentProviderEventDTO1.setId(null);
        assertThat(paymentProviderEventDTO1).isNotEqualTo(paymentProviderEventDTO2);
    }
}
