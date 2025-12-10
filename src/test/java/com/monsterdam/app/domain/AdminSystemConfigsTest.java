package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.AdminSystemConfigsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminSystemConfigsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminSystemConfigs.class);
        AdminSystemConfigs adminSystemConfigs1 = getAdminSystemConfigsSample1();
        AdminSystemConfigs adminSystemConfigs2 = new AdminSystemConfigs();
        assertThat(adminSystemConfigs1).isNotEqualTo(adminSystemConfigs2);

        adminSystemConfigs2.setId(adminSystemConfigs1.getId());
        assertThat(adminSystemConfigs1).isEqualTo(adminSystemConfigs2);

        adminSystemConfigs2 = getAdminSystemConfigsSample2();
        assertThat(adminSystemConfigs1).isNotEqualTo(adminSystemConfigs2);
    }
}
