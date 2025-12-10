package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyWithdrawDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyWithdrawDTO.class);
        MoneyWithdrawDTO moneyWithdrawDTO1 = new MoneyWithdrawDTO();
        moneyWithdrawDTO1.setId(1L);
        MoneyWithdrawDTO moneyWithdrawDTO2 = new MoneyWithdrawDTO();
        assertThat(moneyWithdrawDTO1).isNotEqualTo(moneyWithdrawDTO2);
        moneyWithdrawDTO2.setId(moneyWithdrawDTO1.getId());
        assertThat(moneyWithdrawDTO1).isEqualTo(moneyWithdrawDTO2);
        moneyWithdrawDTO2.setId(2L);
        assertThat(moneyWithdrawDTO1).isNotEqualTo(moneyWithdrawDTO2);
        moneyWithdrawDTO1.setId(null);
        assertThat(moneyWithdrawDTO1).isNotEqualTo(moneyWithdrawDTO2);
    }
}
