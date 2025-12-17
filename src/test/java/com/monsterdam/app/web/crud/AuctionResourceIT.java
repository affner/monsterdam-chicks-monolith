package com.monsterdam.app.web.crud;

import static com.monsterdam.app.domain.AuctionAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static com.monsterdam.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.Auction;
import com.monsterdam.app.domain.enumeration.AuctionStatus;
import com.monsterdam.app.repository.AuctionRepository;
import com.monsterdam.app.service.dto.AuctionDTO;
import com.monsterdam.app.service.mapper.AuctionMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AuctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuctionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_STARTING_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_STARTING_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_PRICE = new BigDecimal(2);

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AuctionStatus DEFAULT_AUCTION_STATUS = AuctionStatus.ACTIVE;
    private static final AuctionStatus UPDATED_AUCTION_STATUS = AuctionStatus.FINISHED;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/crud/auctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionMapper auctionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuctionMockMvc;

    private Auction auction;

    private Auction insertedAuction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auction createEntity() {
        return new Auction()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .startingPrice(DEFAULT_STARTING_PRICE)
            .currentPrice(DEFAULT_CURRENT_PRICE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .auctionStatus(DEFAULT_AUCTION_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .deletedDate(DEFAULT_DELETED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auction createUpdatedEntity() {
        return new Auction()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startingPrice(UPDATED_STARTING_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .auctionStatus(UPDATED_AUCTION_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
    }

    @BeforeEach
    void initTest() {
        auction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAuction != null) {
            auctionRepository.delete(insertedAuction);
            insertedAuction = null;
        }
    }

    @Test
    @Transactional
    void createAuction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Auction
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);
        var returnedAuctionDTO = om.readValue(
            restAuctionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AuctionDTO.class
        );

        // Validate the Auction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAuction = auctionMapper.toEntity(returnedAuctionDTO);
        assertAuctionUpdatableFieldsEquals(returnedAuction, getPersistedAuction(returnedAuction));

        insertedAuction = returnedAuction;
    }

    @Test
    @Transactional
    void createAuctionWithExistingId() throws Exception {
        // Create the Auction with an existing ID
        auction.setId(1L);
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auction.setTitle(null);

        // Create the Auction, which fails.
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartingPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auction.setStartingPrice(null);

        // Create the Auction, which fails.
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auction.setStartDate(null);

        // Create the Auction, which fails.
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auction.setEndDate(null);

        // Create the Auction, which fails.
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAuctionStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auction.setAuctionStatus(null);

        // Create the Auction, which fails.
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auction.setCreatedDate(null);

        // Create the Auction, which fails.
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuctions() throws Exception {
        // Initialize the database
        insertedAuction = auctionRepository.saveAndFlush(auction);

        // Get all the auctionList
        restAuctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auction.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startingPrice").value(hasItem(sameNumber(DEFAULT_STARTING_PRICE))))
            .andExpect(jsonPath("$.[*].currentPrice").value(hasItem(sameNumber(DEFAULT_CURRENT_PRICE))))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].auctionStatus").value(hasItem(DEFAULT_AUCTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].deletedDate").value(hasItem(DEFAULT_DELETED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAuction() throws Exception {
        // Initialize the database
        insertedAuction = auctionRepository.saveAndFlush(auction);

        // Get the auction
        restAuctionMockMvc
            .perform(get(ENTITY_API_URL_ID, auction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auction.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startingPrice").value(sameNumber(DEFAULT_STARTING_PRICE)))
            .andExpect(jsonPath("$.currentPrice").value(sameNumber(DEFAULT_CURRENT_PRICE)))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.auctionStatus").value(DEFAULT_AUCTION_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.deletedDate").value(DEFAULT_DELETED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAuction() throws Exception {
        // Get the auction
        restAuctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuction() throws Exception {
        // Initialize the database
        insertedAuction = auctionRepository.saveAndFlush(auction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auction
        Auction updatedAuction = auctionRepository.findById(auction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAuction are not directly saved in db
        em.detach(updatedAuction);
        updatedAuction
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startingPrice(UPDATED_STARTING_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .auctionStatus(UPDATED_AUCTION_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);
        AuctionDTO auctionDTO = auctionMapper.toDto(updatedAuction);

        restAuctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auctionDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Auction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAuctionToMatchAllProperties(updatedAuction);
    }

    @Test
    @Transactional
    void putNonExistingAuction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auction.setId(longCount.incrementAndGet());

        // Create the Auction
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auctionDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auction.setId(longCount.incrementAndGet());

        // Create the Auction
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(auctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auction.setId(longCount.incrementAndGet());

        // Create the Auction
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Auction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuctionWithPatch() throws Exception {
        // Initialize the database
        insertedAuction = auctionRepository.saveAndFlush(auction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auction using partial update
        Auction partialUpdatedAuction = new Auction();
        partialUpdatedAuction.setId(auction.getId());

        partialUpdatedAuction
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startingPrice(UPDATED_STARTING_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .endDate(UPDATED_END_DATE)
            .auctionStatus(UPDATED_AUCTION_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restAuctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuction))
            )
            .andExpect(status().isOk());

        // Validate the Auction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuctionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAuction, auction), getPersistedAuction(auction));
    }

    @Test
    @Transactional
    void fullUpdateAuctionWithPatch() throws Exception {
        // Initialize the database
        insertedAuction = auctionRepository.saveAndFlush(auction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auction using partial update
        Auction partialUpdatedAuction = new Auction();
        partialUpdatedAuction.setId(auction.getId());

        partialUpdatedAuction
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startingPrice(UPDATED_STARTING_PRICE)
            .currentPrice(UPDATED_CURRENT_PRICE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .auctionStatus(UPDATED_AUCTION_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .deletedDate(UPDATED_DELETED_DATE);

        restAuctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuction))
            )
            .andExpect(status().isOk());

        // Validate the Auction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuctionUpdatableFieldsEquals(partialUpdatedAuction, getPersistedAuction(partialUpdatedAuction));
    }

    @Test
    @Transactional
    void patchNonExistingAuction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auction.setId(longCount.incrementAndGet());

        // Create the Auction
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, auctionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(auctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auction.setId(longCount.incrementAndGet());

        // Create the Auction
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(auctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auction.setId(longCount.incrementAndGet());

        // Create the Auction
        AuctionDTO auctionDTO = auctionMapper.toDto(auction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(auctionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Auction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuction() throws Exception {
        // Initialize the database
        insertedAuction = auctionRepository.saveAndFlush(auction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the auction
        restAuctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, auction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return auctionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Auction getPersistedAuction(Auction auction) {
        return auctionRepository.findById(auction.getId()).orElseThrow();
    }

    protected void assertPersistedAuctionToMatchAllProperties(Auction expectedAuction) {
        assertAuctionAllPropertiesEquals(expectedAuction, getPersistedAuction(expectedAuction));
    }

    protected void assertPersistedAuctionToMatchUpdatableProperties(Auction expectedAuction) {
        assertAuctionAllUpdatablePropertiesEquals(expectedAuction, getPersistedAuction(expectedAuction));
    }
}
