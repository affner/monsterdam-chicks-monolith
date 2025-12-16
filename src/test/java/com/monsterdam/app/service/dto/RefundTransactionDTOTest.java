package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RefundTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RefundTransactionDTO.class);
        RefundTransactionDTO refundTransactionDTO1 = new RefundTransactionDTO();
        refundTransactionDTO1.setId(1L);
        RefundTransactionDTO refundTransactionDTO2 = new RefundTransactionDTO();
        assertThat(refundTransactionDTO1).isNotEqualTo(refundTransactionDTO2);
        refundTransactionDTO2.setId(refundTransactionDTO1.getId());
        assertThat(refundTransactionDTO1).isEqualTo(refundTransactionDTO2);
        refundTransactionDTO2.setId(2L);
        assertThat(refundTransactionDTO1).isNotEqualTo(refundTransactionDTO2);
        refundTransactionDTO1.setId(null);
        assertThat(refundTransactionDTO1).isNotEqualTo(refundTransactionDTO2);
    }
}
