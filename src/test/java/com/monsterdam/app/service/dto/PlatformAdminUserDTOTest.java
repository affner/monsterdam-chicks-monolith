package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlatformAdminUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlatformAdminUserDTO.class);
        PlatformAdminUserDTO platformAdminUserDTO1 = new PlatformAdminUserDTO();
        platformAdminUserDTO1.setId(1L);
        PlatformAdminUserDTO platformAdminUserDTO2 = new PlatformAdminUserDTO();
        assertThat(platformAdminUserDTO1).isNotEqualTo(platformAdminUserDTO2);
        platformAdminUserDTO2.setId(platformAdminUserDTO1.getId());
        assertThat(platformAdminUserDTO1).isEqualTo(platformAdminUserDTO2);
        platformAdminUserDTO2.setId(2L);
        assertThat(platformAdminUserDTO1).isNotEqualTo(platformAdminUserDTO2);
        platformAdminUserDTO1.setId(null);
        assertThat(platformAdminUserDTO1).isNotEqualTo(platformAdminUserDTO2);
    }
}
