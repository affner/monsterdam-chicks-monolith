package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionPlanOfferDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionPlanOfferDTO.class);
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO1 = new SubscriptionPlanOfferDTO();
        subscriptionPlanOfferDTO1.setId(1L);
        SubscriptionPlanOfferDTO subscriptionPlanOfferDTO2 = new SubscriptionPlanOfferDTO();
        assertThat(subscriptionPlanOfferDTO1).isNotEqualTo(subscriptionPlanOfferDTO2);
        subscriptionPlanOfferDTO2.setId(subscriptionPlanOfferDTO1.getId());
        assertThat(subscriptionPlanOfferDTO1).isEqualTo(subscriptionPlanOfferDTO2);
        subscriptionPlanOfferDTO2.setId(2L);
        assertThat(subscriptionPlanOfferDTO1).isNotEqualTo(subscriptionPlanOfferDTO2);
        subscriptionPlanOfferDTO1.setId(null);
        assertThat(subscriptionPlanOfferDTO1).isNotEqualTo(subscriptionPlanOfferDTO2);
    }
}
