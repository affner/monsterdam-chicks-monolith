package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.HelpRelatedArticleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpRelatedArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpRelatedArticle.class);
        HelpRelatedArticle helpRelatedArticle1 = getHelpRelatedArticleSample1();
        HelpRelatedArticle helpRelatedArticle2 = new HelpRelatedArticle();
        assertThat(helpRelatedArticle1).isNotEqualTo(helpRelatedArticle2);

        helpRelatedArticle2.setId(helpRelatedArticle1.getId());
        assertThat(helpRelatedArticle1).isEqualTo(helpRelatedArticle2);

        helpRelatedArticle2 = getHelpRelatedArticleSample2();
        assertThat(helpRelatedArticle1).isNotEqualTo(helpRelatedArticle2);
    }
}
