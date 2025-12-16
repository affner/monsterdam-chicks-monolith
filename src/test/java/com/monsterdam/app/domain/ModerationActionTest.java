package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.ModerationActionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModerationActionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModerationAction.class);
        ModerationAction moderationAction1 = getModerationActionSample1();
        ModerationAction moderationAction2 = new ModerationAction();
        assertThat(moderationAction1).isNotEqualTo(moderationAction2);

        moderationAction2.setId(moderationAction1.getId());
        assertThat(moderationAction1).isEqualTo(moderationAction2);

        moderationAction2 = getModerationActionSample2();
        assertThat(moderationAction1).isNotEqualTo(moderationAction2);
    }
}
