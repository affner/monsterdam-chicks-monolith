package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.AdminAnnouncementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminAnnouncementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminAnnouncement.class);
        AdminAnnouncement adminAnnouncement1 = getAdminAnnouncementSample1();
        AdminAnnouncement adminAnnouncement2 = new AdminAnnouncement();
        assertThat(adminAnnouncement1).isNotEqualTo(adminAnnouncement2);

        adminAnnouncement2.setId(adminAnnouncement1.getId());
        assertThat(adminAnnouncement1).isEqualTo(adminAnnouncement2);

        adminAnnouncement2 = getAdminAnnouncementSample2();
        assertThat(adminAnnouncement1).isNotEqualTo(adminAnnouncement2);
    }
}
