package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViewerWalletDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewerWalletDTO.class);
        ViewerWalletDTO viewerWalletDTO1 = new ViewerWalletDTO();
        viewerWalletDTO1.setId(1L);
        ViewerWalletDTO viewerWalletDTO2 = new ViewerWalletDTO();
        assertThat(viewerWalletDTO1).isNotEqualTo(viewerWalletDTO2);
        viewerWalletDTO2.setId(viewerWalletDTO1.getId());
        assertThat(viewerWalletDTO1).isEqualTo(viewerWalletDTO2);
        viewerWalletDTO2.setId(2L);
        assertThat(viewerWalletDTO1).isNotEqualTo(viewerWalletDTO2);
        viewerWalletDTO1.setId(null);
        assertThat(viewerWalletDTO1).isNotEqualTo(viewerWalletDTO2);
    }
}
