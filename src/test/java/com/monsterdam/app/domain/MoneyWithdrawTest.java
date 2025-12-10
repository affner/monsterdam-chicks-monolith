package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.MoneyWithdrawTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MoneyWithdrawTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MoneyWithdraw.class);
        MoneyWithdraw moneyWithdraw1 = getMoneyWithdrawSample1();
        MoneyWithdraw moneyWithdraw2 = new MoneyWithdraw();
        assertThat(moneyWithdraw1).isNotEqualTo(moneyWithdraw2);

        moneyWithdraw2.setId(moneyWithdraw1.getId());
        assertThat(moneyWithdraw1).isEqualTo(moneyWithdraw2);

        moneyWithdraw2 = getMoneyWithdrawSample2();
        assertThat(moneyWithdraw1).isNotEqualTo(moneyWithdraw2);
    }

    @Test
    void creatorTest() {
        MoneyWithdraw moneyWithdraw = getMoneyWithdrawRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        moneyWithdraw.setCreator(userLiteBack);
        assertThat(moneyWithdraw.getCreator()).isEqualTo(userLiteBack);

        moneyWithdraw.creator(null);
        assertThat(moneyWithdraw.getCreator()).isNull();
    }
}
