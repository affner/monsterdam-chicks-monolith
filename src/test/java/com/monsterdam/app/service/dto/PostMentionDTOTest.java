package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostMentionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostMentionDTO.class);
        PostMentionDTO postMentionDTO1 = new PostMentionDTO();
        postMentionDTO1.setId(1L);
        PostMentionDTO postMentionDTO2 = new PostMentionDTO();
        assertThat(postMentionDTO1).isNotEqualTo(postMentionDTO2);
        postMentionDTO2.setId(postMentionDTO1.getId());
        assertThat(postMentionDTO1).isEqualTo(postMentionDTO2);
        postMentionDTO2.setId(2L);
        assertThat(postMentionDTO1).isNotEqualTo(postMentionDTO2);
        postMentionDTO1.setId(null);
        assertThat(postMentionDTO1).isNotEqualTo(postMentionDTO2);
    }
}
