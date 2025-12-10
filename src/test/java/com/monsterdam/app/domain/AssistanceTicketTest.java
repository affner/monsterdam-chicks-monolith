package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.AssistanceTicketTestSamples.*;
import static com.monsterdam.app.domain.IdentityDocumentReviewTestSamples.*;
import static com.monsterdam.app.domain.PlatformAdminUserTestSamples.*;
import static com.monsterdam.app.domain.UserReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssistanceTicketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssistanceTicket.class);
        AssistanceTicket assistanceTicket1 = getAssistanceTicketSample1();
        AssistanceTicket assistanceTicket2 = new AssistanceTicket();
        assertThat(assistanceTicket1).isNotEqualTo(assistanceTicket2);

        assistanceTicket2.setId(assistanceTicket1.getId());
        assertThat(assistanceTicket1).isEqualTo(assistanceTicket2);

        assistanceTicket2 = getAssistanceTicketSample2();
        assertThat(assistanceTicket1).isNotEqualTo(assistanceTicket2);
    }

    @Test
    void assignedAdminTest() {
        AssistanceTicket assistanceTicket = getAssistanceTicketRandomSampleGenerator();
        PlatformAdminUser platformAdminUserBack = getPlatformAdminUserRandomSampleGenerator();

        assistanceTicket.setAssignedAdmin(platformAdminUserBack);
        assertThat(assistanceTicket.getAssignedAdmin()).isEqualTo(platformAdminUserBack);

        assistanceTicket.assignedAdmin(null);
        assertThat(assistanceTicket.getAssignedAdmin()).isNull();
    }

    @Test
    void reportTest() {
        AssistanceTicket assistanceTicket = getAssistanceTicketRandomSampleGenerator();
        UserReport userReportBack = getUserReportRandomSampleGenerator();

        assistanceTicket.setReport(userReportBack);
        assertThat(assistanceTicket.getReport()).isEqualTo(userReportBack);

        assistanceTicket.report(null);
        assertThat(assistanceTicket.getReport()).isNull();
    }

    @Test
    void documentsReviewTest() {
        AssistanceTicket assistanceTicket = getAssistanceTicketRandomSampleGenerator();
        IdentityDocumentReview identityDocumentReviewBack = getIdentityDocumentReviewRandomSampleGenerator();

        assistanceTicket.setDocumentsReview(identityDocumentReviewBack);
        assertThat(assistanceTicket.getDocumentsReview()).isEqualTo(identityDocumentReviewBack);

        assistanceTicket.documentsReview(null);
        assertThat(assistanceTicket.getDocumentsReview()).isNull();
    }
}
