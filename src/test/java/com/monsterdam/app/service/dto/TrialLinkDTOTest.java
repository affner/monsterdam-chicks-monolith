package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrialLinkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrialLinkDTO.class);
        TrialLinkDTO trialLinkDTO1 = new TrialLinkDTO();
        trialLinkDTO1.setId(1L);
        TrialLinkDTO trialLinkDTO2 = new TrialLinkDTO();
        assertThat(trialLinkDTO1).isNotEqualTo(trialLinkDTO2);
        trialLinkDTO2.setId(trialLinkDTO1.getId());
        assertThat(trialLinkDTO1).isEqualTo(trialLinkDTO2);
        trialLinkDTO2.setId(2L);
        assertThat(trialLinkDTO1).isNotEqualTo(trialLinkDTO2);
        trialLinkDTO1.setId(null);
        assertThat(trialLinkDTO1).isNotEqualTo(trialLinkDTO2);
    }
}
