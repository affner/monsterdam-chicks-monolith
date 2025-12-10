package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyEarningDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyEarningDTO.class);
        MoneyEarningDTO moneyEarningDTO1 = new MoneyEarningDTO();
        moneyEarningDTO1.setId(1L);
        MoneyEarningDTO moneyEarningDTO2 = new MoneyEarningDTO();
        assertThat(moneyEarningDTO1).isNotEqualTo(moneyEarningDTO2);
        moneyEarningDTO2.setId(moneyEarningDTO1.getId());
        assertThat(moneyEarningDTO1).isEqualTo(moneyEarningDTO2);
        moneyEarningDTO2.setId(2L);
        assertThat(moneyEarningDTO1).isNotEqualTo(moneyEarningDTO2);
        moneyEarningDTO1.setId(null);
        assertThat(moneyEarningDTO1).isNotEqualTo(moneyEarningDTO2);
    }
}
