package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.HelpCategoryTestSamples.*;
import static com.monsterdam.app.domain.HelpSubcategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HelpSubcategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HelpSubcategory.class);
        HelpSubcategory helpSubcategory1 = getHelpSubcategorySample1();
        HelpSubcategory helpSubcategory2 = new HelpSubcategory();
        assertThat(helpSubcategory1).isNotEqualTo(helpSubcategory2);

        helpSubcategory2.setId(helpSubcategory1.getId());
        assertThat(helpSubcategory1).isEqualTo(helpSubcategory2);

        helpSubcategory2 = getHelpSubcategorySample2();
        assertThat(helpSubcategory1).isNotEqualTo(helpSubcategory2);
    }

    @Test
    void categoryTest() {
        HelpSubcategory helpSubcategory = getHelpSubcategoryRandomSampleGenerator();
        HelpCategory helpCategoryBack = getHelpCategoryRandomSampleGenerator();

        helpSubcategory.setCategory(helpCategoryBack);
        assertThat(helpSubcategory.getCategory()).isEqualTo(helpCategoryBack);

        helpSubcategory.category(null);
        assertThat(helpSubcategory.getCategory()).isNull();
    }
}
