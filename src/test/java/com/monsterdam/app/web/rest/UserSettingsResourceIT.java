package com.monsterdam.app.web.rest;

import static com.monsterdam.app.domain.UserSettingsAsserts.*;
import static com.monsterdam.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterdam.app.IntegrationTest;
import com.monsterdam.app.domain.UserSettings;
import com.monsterdam.app.domain.enumeration.UserLanguage;
import com.monsterdam.app.repository.UserSettingsRepository;
import com.monsterdam.app.service.dto.UserSettingsDTO;
import com.monsterdam.app.service.mapper.UserSettingsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserSettingsResource} REST controller.
 */
@IntegrationTest
@Disabled("Cyclic required relationships detected")
@AutoConfigureMockMvc
@WithMockUser
class UserSettingsResourceIT {

    private static final Boolean DEFAULT_DARK_MODE = false;
    private static final Boolean UPDATED_DARK_MODE = true;

    private static final UserLanguage DEFAULT_LANGUAGE = UserLanguage.ES;
    private static final UserLanguage UPDATED_LANGUAGE = UserLanguage.EN;

    private static final Boolean DEFAULT_CONTENT_FILTER = false;
    private static final Boolean UPDATED_CONTENT_FILTER = true;

    private static final Integer DEFAULT_MESSAGE_BLUR_INTENSITY = 1;
    private static final Integer UPDATED_MESSAGE_BLUR_INTENSITY = 2;

    private static final Boolean DEFAULT_ACTIVITY_STATUS_VISIBILITY = false;
    private static final Boolean UPDATED_ACTIVITY_STATUS_VISIBILITY = true;

    private static final Boolean DEFAULT_TWO_FACTOR_AUTHENTICATION = false;
    private static final Boolean UPDATED_TWO_FACTOR_AUTHENTICATION = true;

    private static final Integer DEFAULT_SESSIONS_ACTIVE_COUNT = 1;
    private static final Integer UPDATED_SESSIONS_ACTIVE_COUNT = 2;

    private static final Boolean DEFAULT_EMAIL_NOTIFICATIONS = false;
    private static final Boolean UPDATED_EMAIL_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS = false;
    private static final Boolean UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_NEW_MESSAGES = false;
    private static final Boolean UPDATED_NEW_MESSAGES = true;

    private static final Boolean DEFAULT_POST_REPLIES = false;
    private static final Boolean UPDATED_POST_REPLIES = true;

    private static final Boolean DEFAULT_POST_LIKES = false;
    private static final Boolean UPDATED_POST_LIKES = true;

    private static final Boolean DEFAULT_NEW_FOLLOWERS = false;
    private static final Boolean UPDATED_NEW_FOLLOWERS = true;

    private static final Boolean DEFAULT_SMS_NEW_STREAM = false;
    private static final Boolean UPDATED_SMS_NEW_STREAM = true;

    private static final Boolean DEFAULT_TOAST_NEW_COMMENT = false;
    private static final Boolean UPDATED_TOAST_NEW_COMMENT = true;

    private static final Boolean DEFAULT_TOAST_NEW_LIKES = false;
    private static final Boolean UPDATED_TOAST_NEW_LIKES = true;

    private static final Boolean DEFAULT_TOAST_NEW_STREAM = false;
    private static final Boolean UPDATED_TOAST_NEW_STREAM = true;

    private static final Boolean DEFAULT_SITE_NEW_COMMENT = false;
    private static final Boolean UPDATED_SITE_NEW_COMMENT = true;

    private static final Boolean DEFAULT_SITE_NEW_LIKES = false;
    private static final Boolean UPDATED_SITE_NEW_LIKES = true;

    private static final Boolean DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS = false;
    private static final Boolean UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS = true;

    private static final Boolean DEFAULT_SITE_NEW_STREAM = false;
    private static final Boolean UPDATED_SITE_NEW_STREAM = true;

    private static final Boolean DEFAULT_SITE_UPCOMING_STREAM_REMINDERS = false;
    private static final Boolean UPDATED_SITE_UPCOMING_STREAM_REMINDERS = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/user-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserSettingsMapper userSettingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSettingsMockMvc;

    private UserSettings userSettings;

    private UserSettings insertedUserSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSettings createEntity(EntityManager em) {
        UserSettings userSettings = new UserSettings()
            .darkMode(DEFAULT_DARK_MODE)
            .language(DEFAULT_LANGUAGE)
            .contentFilter(DEFAULT_CONTENT_FILTER)
            .messageBlurIntensity(DEFAULT_MESSAGE_BLUR_INTENSITY)
            .activityStatusVisibility(DEFAULT_ACTIVITY_STATUS_VISIBILITY)
            .twoFactorAuthentication(DEFAULT_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(DEFAULT_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(DEFAULT_EMAIL_NOTIFICATIONS)
            .importantSubscriptionNotifications(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)
            .newMessages(DEFAULT_NEW_MESSAGES)
            .postReplies(DEFAULT_POST_REPLIES)
            .postLikes(DEFAULT_POST_LIKES)
            .newFollowers(DEFAULT_NEW_FOLLOWERS)
            .smsNewStream(DEFAULT_SMS_NEW_STREAM)
            .toastNewComment(DEFAULT_TOAST_NEW_COMMENT)
            .toastNewLikes(DEFAULT_TOAST_NEW_LIKES)
            .toastNewStream(DEFAULT_TOAST_NEW_STREAM)
            .siteNewComment(DEFAULT_SITE_NEW_COMMENT)
            .siteNewLikes(DEFAULT_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteNewStream(DEFAULT_SITE_NEW_STREAM)
            .siteUpcomingStreamReminders(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return userSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSettings createUpdatedEntity(EntityManager em) {
        UserSettings updatedUserSettings = new UserSettings()
            .darkMode(UPDATED_DARK_MODE)
            .language(UPDATED_LANGUAGE)
            .contentFilter(UPDATED_CONTENT_FILTER)
            .messageBlurIntensity(UPDATED_MESSAGE_BLUR_INTENSITY)
            .activityStatusVisibility(UPDATED_ACTIVITY_STATUS_VISIBILITY)
            .twoFactorAuthentication(UPDATED_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(UPDATED_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .importantSubscriptionNotifications(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)
            .newMessages(UPDATED_NEW_MESSAGES)
            .postReplies(UPDATED_POST_REPLIES)
            .postLikes(UPDATED_POST_LIKES)
            .newFollowers(UPDATED_NEW_FOLLOWERS)
            .smsNewStream(UPDATED_SMS_NEW_STREAM)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewLikes(UPDATED_TOAST_NEW_LIKES)
            .toastNewStream(UPDATED_TOAST_NEW_STREAM)
            .siteNewComment(UPDATED_SITE_NEW_COMMENT)
            .siteNewLikes(UPDATED_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteNewStream(UPDATED_SITE_NEW_STREAM)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return updatedUserSettings;
    }

    @BeforeEach
    void initTest() {
        userSettings = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserSettings != null) {
            userSettingsRepository.delete(insertedUserSettings);
            insertedUserSettings = null;
        }
    }

    @Test
    @Transactional
    void createUserSettings() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);
        var returnedUserSettingsDTO = om.readValue(
            restUserSettingsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserSettingsDTO.class
        );

        // Validate the UserSettings in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserSettings = userSettingsMapper.toEntity(returnedUserSettingsDTO);
        assertUserSettingsUpdatableFieldsEquals(returnedUserSettings, getPersistedUserSettings(returnedUserSettings));

        insertedUserSettings = returnedUserSettings;
    }

    @Test
    @Transactional
    void createUserSettingsWithExistingId() throws Exception {
        // Create the UserSettings with an existing ID
        userSettings.setId(1L);
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDarkModeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setDarkMode(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setLanguage(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentFilterIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setContentFilter(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivityStatusVisibilityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setActivityStatusVisibility(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTwoFactorAuthenticationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setTwoFactorAuthentication(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailNotificationsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setEmailNotifications(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImportantSubscriptionNotificationsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setImportantSubscriptionNotifications(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewMessagesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setNewMessages(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostRepliesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setPostReplies(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostLikesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setPostLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewFollowersIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setNewFollowers(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSmsNewStreamIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setSmsNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToastNewCommentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setToastNewComment(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToastNewLikesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setToastNewLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToastNewStreamIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setToastNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteNewCommentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setSiteNewComment(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteNewLikesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setSiteNewLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteDiscountsFromFollowedUsersIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setSiteDiscountsFromFollowedUsers(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteNewStreamIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setSiteNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteUpcomingStreamRemindersIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setSiteUpcomingStreamReminders(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setCreatedDate(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSettings.setIsDeleted(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserSettings() throws Exception {
        // Initialize the database
        insertedUserSettings = userSettingsRepository.saveAndFlush(userSettings);

        // Get all the userSettingsList
        restUserSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].darkMode").value(hasItem(DEFAULT_DARK_MODE)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].contentFilter").value(hasItem(DEFAULT_CONTENT_FILTER)))
            .andExpect(jsonPath("$.[*].messageBlurIntensity").value(hasItem(DEFAULT_MESSAGE_BLUR_INTENSITY)))
            .andExpect(jsonPath("$.[*].activityStatusVisibility").value(hasItem(DEFAULT_ACTIVITY_STATUS_VISIBILITY)))
            .andExpect(jsonPath("$.[*].twoFactorAuthentication").value(hasItem(DEFAULT_TWO_FACTOR_AUTHENTICATION)))
            .andExpect(jsonPath("$.[*].sessionsActiveCount").value(hasItem(DEFAULT_SESSIONS_ACTIVE_COUNT)))
            .andExpect(jsonPath("$.[*].emailNotifications").value(hasItem(DEFAULT_EMAIL_NOTIFICATIONS)))
            .andExpect(jsonPath("$.[*].importantSubscriptionNotifications").value(hasItem(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)))
            .andExpect(jsonPath("$.[*].newMessages").value(hasItem(DEFAULT_NEW_MESSAGES)))
            .andExpect(jsonPath("$.[*].postReplies").value(hasItem(DEFAULT_POST_REPLIES)))
            .andExpect(jsonPath("$.[*].postLikes").value(hasItem(DEFAULT_POST_LIKES)))
            .andExpect(jsonPath("$.[*].newFollowers").value(hasItem(DEFAULT_NEW_FOLLOWERS)))
            .andExpect(jsonPath("$.[*].smsNewStream").value(hasItem(DEFAULT_SMS_NEW_STREAM)))
            .andExpect(jsonPath("$.[*].toastNewComment").value(hasItem(DEFAULT_TOAST_NEW_COMMENT)))
            .andExpect(jsonPath("$.[*].toastNewLikes").value(hasItem(DEFAULT_TOAST_NEW_LIKES)))
            .andExpect(jsonPath("$.[*].toastNewStream").value(hasItem(DEFAULT_TOAST_NEW_STREAM)))
            .andExpect(jsonPath("$.[*].siteNewComment").value(hasItem(DEFAULT_SITE_NEW_COMMENT)))
            .andExpect(jsonPath("$.[*].siteNewLikes").value(hasItem(DEFAULT_SITE_NEW_LIKES)))
            .andExpect(jsonPath("$.[*].siteDiscountsFromFollowedUsers").value(hasItem(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)))
            .andExpect(jsonPath("$.[*].siteNewStream").value(hasItem(DEFAULT_SITE_NEW_STREAM)))
            .andExpect(jsonPath("$.[*].siteUpcomingStreamReminders").value(hasItem(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED)));
    }

    @Test
    @Transactional
    void getUserSettings() throws Exception {
        // Initialize the database
        insertedUserSettings = userSettingsRepository.saveAndFlush(userSettings);

        // Get the userSettings
        restUserSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, userSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSettings.getId().intValue()))
            .andExpect(jsonPath("$.darkMode").value(DEFAULT_DARK_MODE))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.contentFilter").value(DEFAULT_CONTENT_FILTER))
            .andExpect(jsonPath("$.messageBlurIntensity").value(DEFAULT_MESSAGE_BLUR_INTENSITY))
            .andExpect(jsonPath("$.activityStatusVisibility").value(DEFAULT_ACTIVITY_STATUS_VISIBILITY))
            .andExpect(jsonPath("$.twoFactorAuthentication").value(DEFAULT_TWO_FACTOR_AUTHENTICATION))
            .andExpect(jsonPath("$.sessionsActiveCount").value(DEFAULT_SESSIONS_ACTIVE_COUNT))
            .andExpect(jsonPath("$.emailNotifications").value(DEFAULT_EMAIL_NOTIFICATIONS))
            .andExpect(jsonPath("$.importantSubscriptionNotifications").value(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS))
            .andExpect(jsonPath("$.newMessages").value(DEFAULT_NEW_MESSAGES))
            .andExpect(jsonPath("$.postReplies").value(DEFAULT_POST_REPLIES))
            .andExpect(jsonPath("$.postLikes").value(DEFAULT_POST_LIKES))
            .andExpect(jsonPath("$.newFollowers").value(DEFAULT_NEW_FOLLOWERS))
            .andExpect(jsonPath("$.smsNewStream").value(DEFAULT_SMS_NEW_STREAM))
            .andExpect(jsonPath("$.toastNewComment").value(DEFAULT_TOAST_NEW_COMMENT))
            .andExpect(jsonPath("$.toastNewLikes").value(DEFAULT_TOAST_NEW_LIKES))
            .andExpect(jsonPath("$.toastNewStream").value(DEFAULT_TOAST_NEW_STREAM))
            .andExpect(jsonPath("$.siteNewComment").value(DEFAULT_SITE_NEW_COMMENT))
            .andExpect(jsonPath("$.siteNewLikes").value(DEFAULT_SITE_NEW_LIKES))
            .andExpect(jsonPath("$.siteDiscountsFromFollowedUsers").value(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS))
            .andExpect(jsonPath("$.siteNewStream").value(DEFAULT_SITE_NEW_STREAM))
            .andExpect(jsonPath("$.siteUpcomingStreamReminders").value(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED));
    }

    @Test
    @Transactional
    void getNonExistingUserSettings() throws Exception {
        // Get the userSettings
        restUserSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSettings() throws Exception {
        // Initialize the database
        insertedUserSettings = userSettingsRepository.saveAndFlush(userSettings);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSettings
        UserSettings updatedUserSettings = userSettingsRepository.findById(userSettings.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserSettings are not directly saved in db
        em.detach(updatedUserSettings);
        updatedUserSettings
            .darkMode(UPDATED_DARK_MODE)
            .language(UPDATED_LANGUAGE)
            .contentFilter(UPDATED_CONTENT_FILTER)
            .messageBlurIntensity(UPDATED_MESSAGE_BLUR_INTENSITY)
            .activityStatusVisibility(UPDATED_ACTIVITY_STATUS_VISIBILITY)
            .twoFactorAuthentication(UPDATED_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(UPDATED_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .importantSubscriptionNotifications(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)
            .newMessages(UPDATED_NEW_MESSAGES)
            .postReplies(UPDATED_POST_REPLIES)
            .postLikes(UPDATED_POST_LIKES)
            .newFollowers(UPDATED_NEW_FOLLOWERS)
            .smsNewStream(UPDATED_SMS_NEW_STREAM)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewLikes(UPDATED_TOAST_NEW_LIKES)
            .toastNewStream(UPDATED_TOAST_NEW_STREAM)
            .siteNewComment(UPDATED_SITE_NEW_COMMENT)
            .siteNewLikes(UPDATED_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteNewStream(UPDATED_SITE_NEW_STREAM)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(updatedUserSettings);

        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSettingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserSettingsToMatchAllProperties(updatedUserSettings);
    }

    @Test
    @Transactional
    void putNonExistingUserSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSettingsWithPatch() throws Exception {
        // Initialize the database
        insertedUserSettings = userSettingsRepository.saveAndFlush(userSettings);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSettings using partial update
        UserSettings partialUpdatedUserSettings = new UserSettings();
        partialUpdatedUserSettings.setId(userSettings.getId());

        partialUpdatedUserSettings
            .darkMode(UPDATED_DARK_MODE)
            .contentFilter(UPDATED_CONTENT_FILTER)
            .newMessages(UPDATED_NEW_MESSAGES)
            .postReplies(UPDATED_POST_REPLIES)
            .postLikes(UPDATED_POST_LIKES)
            .smsNewStream(UPDATED_SMS_NEW_STREAM)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewStream(UPDATED_TOAST_NEW_STREAM)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSettings))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSettingsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserSettings, userSettings),
            getPersistedUserSettings(userSettings)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserSettingsWithPatch() throws Exception {
        // Initialize the database
        insertedUserSettings = userSettingsRepository.saveAndFlush(userSettings);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSettings using partial update
        UserSettings partialUpdatedUserSettings = new UserSettings();
        partialUpdatedUserSettings.setId(userSettings.getId());

        partialUpdatedUserSettings
            .darkMode(UPDATED_DARK_MODE)
            .language(UPDATED_LANGUAGE)
            .contentFilter(UPDATED_CONTENT_FILTER)
            .messageBlurIntensity(UPDATED_MESSAGE_BLUR_INTENSITY)
            .activityStatusVisibility(UPDATED_ACTIVITY_STATUS_VISIBILITY)
            .twoFactorAuthentication(UPDATED_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(UPDATED_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .importantSubscriptionNotifications(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)
            .newMessages(UPDATED_NEW_MESSAGES)
            .postReplies(UPDATED_POST_REPLIES)
            .postLikes(UPDATED_POST_LIKES)
            .newFollowers(UPDATED_NEW_FOLLOWERS)
            .smsNewStream(UPDATED_SMS_NEW_STREAM)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewLikes(UPDATED_TOAST_NEW_LIKES)
            .toastNewStream(UPDATED_TOAST_NEW_STREAM)
            .siteNewComment(UPDATED_SITE_NEW_COMMENT)
            .siteNewLikes(UPDATED_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteNewStream(UPDATED_SITE_NEW_STREAM)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSettings))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSettingsUpdatableFieldsEquals(partialUpdatedUserSettings, getPersistedUserSettings(partialUpdatedUserSettings));
    }

    @Test
    @Transactional
    void patchNonExistingUserSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSettings() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userSettingsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSettings in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSettings() throws Exception {
        // Initialize the database
        insertedUserSettings = userSettingsRepository.saveAndFlush(userSettings);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userSettings
        restUserSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userSettingsRepository.count();
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

    protected UserSettings getPersistedUserSettings(UserSettings userSettings) {
        return userSettingsRepository.findById(userSettings.getId()).orElseThrow();
    }

    protected void assertPersistedUserSettingsToMatchAllProperties(UserSettings expectedUserSettings) {
        assertUserSettingsAllPropertiesEquals(expectedUserSettings, getPersistedUserSettings(expectedUserSettings));
    }

    protected void assertPersistedUserSettingsToMatchUpdatableProperties(UserSettings expectedUserSettings) {
        assertUserSettingsAllUpdatablePropertiesEquals(expectedUserSettings, getPersistedUserSettings(expectedUserSettings));
    }
}
