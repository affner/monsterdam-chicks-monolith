package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.CountryTestSamples.*;
import static com.monsterdam.app.domain.StateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(State.class);
        State state1 = getStateSample1();
        State state2 = new State();
        assertThat(state1).isNotEqualTo(state2);

        state2.setId(state1.getId());
        assertThat(state1).isEqualTo(state2);

        state2 = getStateSample2();
        assertThat(state1).isNotEqualTo(state2);
    }

    @Test
    void countryTest() {
        State state = getStateRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        state.setCountry(countryBack);
        assertThat(state.getCountry()).isEqualTo(countryBack);

        state.country(null);
        assertThat(state.getCountry()).isNull();
    }
}
