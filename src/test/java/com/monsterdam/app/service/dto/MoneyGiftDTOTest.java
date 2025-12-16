package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyGiftDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyGiftDTO.class);
        MoneyGiftDTO moneyGiftDTO1 = new MoneyGiftDTO();
        moneyGiftDTO1.setId(1L);
        MoneyGiftDTO moneyGiftDTO2 = new MoneyGiftDTO();
        assertThat(moneyGiftDTO1).isNotEqualTo(moneyGiftDTO2);
        moneyGiftDTO2.setId(moneyGiftDTO1.getId());
        assertThat(moneyGiftDTO1).isEqualTo(moneyGiftDTO2);
        moneyGiftDTO2.setId(2L);
        assertThat(moneyGiftDTO1).isNotEqualTo(moneyGiftDTO2);
        moneyGiftDTO1.setId(null);
        assertThat(moneyGiftDTO1).isNotEqualTo(moneyGiftDTO2);
    }
}
