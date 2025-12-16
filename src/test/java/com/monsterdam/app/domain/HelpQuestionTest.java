package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.HelpQuestionTestSamples.*;
import static com.monsterdam.app.domain.HelpSubcategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpQuestion.class);
        HelpQuestion helpQuestion1 = getHelpQuestionSample1();
        HelpQuestion helpQuestion2 = new HelpQuestion();
        assertThat(helpQuestion1).isNotEqualTo(helpQuestion2);

        helpQuestion2.setId(helpQuestion1.getId());
        assertThat(helpQuestion1).isEqualTo(helpQuestion2);

        helpQuestion2 = getHelpQuestionSample2();
        assertThat(helpQuestion1).isNotEqualTo(helpQuestion2);
    }

    @Test
    void subcategoryTest() {
        HelpQuestion helpQuestion = getHelpQuestionRandomSampleGenerator();
        HelpSubcategory helpSubcategoryBack = getHelpSubcategoryRandomSampleGenerator();

        helpQuestion.setSubcategory(helpSubcategoryBack);
        assertThat(helpQuestion.getSubcategory()).isEqualTo(helpSubcategoryBack);

        helpQuestion.subcategory(null);
        assertThat(helpQuestion.getSubcategory()).isNull();
    }
}
