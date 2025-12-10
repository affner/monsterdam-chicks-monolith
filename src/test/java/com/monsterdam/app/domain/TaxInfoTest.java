package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.CountryTestSamples.*;
import static com.monsterdam.app.domain.TaxInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxInfo.class);
        TaxInfo taxInfo1 = getTaxInfoSample1();
        TaxInfo taxInfo2 = new TaxInfo();
        assertThat(taxInfo1).isNotEqualTo(taxInfo2);

        taxInfo2.setId(taxInfo1.getId());
        assertThat(taxInfo1).isEqualTo(taxInfo2);

        taxInfo2 = getTaxInfoSample2();
        assertThat(taxInfo1).isNotEqualTo(taxInfo2);
    }

    @Test
    void countryTest() {
        TaxInfo taxInfo = getTaxInfoRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        taxInfo.setCountry(countryBack);
        assertThat(taxInfo.getCountry()).isEqualTo(countryBack);

        taxInfo.country(null);
        assertThat(taxInfo.getCountry()).isNull();
    }
}
