package com.monsterdam.app.domain;

import static com.monsterdam.app.domain.AuctionTestSamples.*;
import static com.monsterdam.app.domain.UserLiteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.monsterdam.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuctionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Auction.class);
        Auction auction1 = getAuctionSample1();
        Auction auction2 = new Auction();
        assertThat(auction1).isNotEqualTo(auction2);

        auction2.setId(auction1.getId());
        assertThat(auction1).isEqualTo(auction2);

        auction2 = getAuctionSample2();
        assertThat(auction1).isNotEqualTo(auction2);
    }

    @Test
    void creatorTest() {
        Auction auction = getAuctionRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        auction.setCreator(userLiteBack);
        assertThat(auction.getCreator()).isEqualTo(userLiteBack);

        auction.creator(null);
        assertThat(auction.getCreator()).isNull();
    }

    @Test
    void winnerTest() {
        Auction auction = getAuctionRandomSampleGenerator();
        UserLite userLiteBack = getUserLiteRandomSampleGenerator();

        auction.setWinner(userLiteBack);
        assertThat(auction.getWinner()).isEqualTo(userLiteBack);

        auction.winner(null);
        assertThat(auction.getWinner()).isNull();
    }
}
