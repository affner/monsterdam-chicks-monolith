package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.LedgerEntryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LedgerEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LedgerEntry.class);
        LedgerEntry ledgerEntry1 = getLedgerEntrySample1();
        LedgerEntry ledgerEntry2 = new LedgerEntry();
        assertThat(ledgerEntry1).isNotEqualTo(ledgerEntry2);

        ledgerEntry2.setId(ledgerEntry1.getId());
        assertThat(ledgerEntry1).isEqualTo(ledgerEntry2);

        ledgerEntry2 = getLedgerEntrySample2();
        assertThat(ledgerEntry1).isNotEqualTo(ledgerEntry2);
    }
}
