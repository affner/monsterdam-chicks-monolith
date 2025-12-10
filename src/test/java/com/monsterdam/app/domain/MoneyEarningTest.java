package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.MoneyEarningTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyEarningTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyEarning.class);
        MoneyEarning moneyEarning1 = getMoneyEarningSample1();
        MoneyEarning moneyEarning2 = new MoneyEarning();
        assertThat(moneyEarning1).isNotEqualTo(moneyEarning2);

        moneyEarning2.setId(moneyEarning1.getId());
        assertThat(moneyEarning1).isEqualTo(moneyEarning2);

        moneyEarning2 = getMoneyEarningSample2();
        assertThat(moneyEarning1).isNotEqualTo(moneyEarning2);
    }

    @Test
    void creatorTest() {
        MoneyEarning moneyEarning = getMoneyEarningRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        moneyEarning.setCreator(userLiteBack);
        assertThat(moneyEarning.getCreator()).isEqualTo(userLiteBack);

        moneyEarning.creator(null);
        assertThat(moneyEarning.getCreator()).isNull();
    }
}
