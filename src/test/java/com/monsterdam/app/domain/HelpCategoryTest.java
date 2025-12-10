package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.HelpCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpCategory.class);
        HelpCategory helpCategory1 = getHelpCategorySample1();
        HelpCategory helpCategory2 = new HelpCategory();
        assertThat(helpCategory1).isNotEqualTo(helpCategory2);

        helpCategory2.setId(helpCategory1.getId());
        assertThat(helpCategory1).isEqualTo(helpCategory2);

        helpCategory2 = getHelpCategorySample2();
        assertThat(helpCategory1).isNotEqualTo(helpCategory2);
    }
}
