package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.CountryTestSamples.*;
import static com.monsterdam.app.domain.StateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void statesTest() {
        Country country = getCountryRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        country.addStates(stateBack);
        assertThat(country.getStates()).containsOnly(stateBack);
        assertThat(stateBack.getCountry()).isEqualTo(country);

        country.removeStates(stateBack);
        assertThat(country.getStates()).doesNotContain(stateBack);
        assertThat(stateBack.getCountry()).isNull();

        country.states(new HashSet<>(Set.of(stateBack)));
        assertThat(country.getStates()).containsOnly(stateBack);
        assertThat(stateBack.getCountry()).isEqualTo(country);

        country.setStates(new HashSet<>());
        assertThat(country.getStates()).doesNotContain(stateBack);
        assertThat(stateBack.getCountry()).isNull();
    }
}
