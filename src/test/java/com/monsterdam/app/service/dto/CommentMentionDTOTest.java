package com.monsterdam.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentMentionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentMentionDTO.class);
        CommentMentionDTO commentMentionDTO1 = new CommentMentionDTO();
        commentMentionDTO1.setId(1L);
        CommentMentionDTO commentMentionDTO2 = new CommentMentionDTO();
        assertThat(commentMentionDTO1).isNotEqualTo(commentMentionDTO2);
        commentMentionDTO2.setId(commentMentionDTO1.getId());
        assertThat(commentMentionDTO1).isEqualTo(commentMentionDTO2);
        commentMentionDTO2.setId(2L);
        assertThat(commentMentionDTO1).isNotEqualTo(commentMentionDTO2);
        commentMentionDTO1.setId(null);
        assertThat(commentMentionDTO1).isNotEqualTo(commentMentionDTO2);
    }
}
