import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('UserLite e2e test', () => {
  const userLitePageUrl = '/user-lite';
  const userLitePageUrlPattern = new RegExp('/user-lite(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const userLiteSample = {"birthDate":"2025-12-09","gender":"FEMALE","createdDate":"2025-12-09T21:41:04.832Z","isDeleted":true,"nickName":"lpq","fullName":"Z"};

  let userLite;
  // let userProfile;
  // let userSettings;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-profiles',
      body: {"emailContact":"mostly","profilePhotoS3Key":"tattered","coverPhotoS3Key":"membership strictly overdue","mainContentUrl":"bore","mobilePhone":"forgo reopen","websiteUrl":"charming tuxedo","amazonWishlistUrl":"worth to","lastLoginDate":"2025-12-10T08:39:11.027Z","biography":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isFree":true,"createdDate":"2025-12-09T13:07:32.988Z","lastModifiedDate":"2025-12-10T07:36:07.764Z","createdBy":"eek hmph arcade","lastModifiedBy":"next pharmacopoeia loyalty","isDeleted":false},
    }).then(({ body }) => {
      userProfile = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-settings',
      body: {"darkMode":false,"language":"FR","contentFilter":true,"messageBlurIntensity":9040,"activityStatusVisibility":true,"twoFactorAuthentication":true,"sessionsActiveCount":21399,"emailNotifications":true,"importantSubscriptionNotifications":false,"newMessages":true,"postReplies":false,"postLikes":true,"newFollowers":true,"smsNewStream":true,"toastNewComment":false,"toastNewLikes":true,"toastNewStream":true,"siteNewComment":true,"siteNewLikes":true,"siteDiscountsFromFollowedUsers":false,"siteNewStream":false,"siteUpcomingStreamReminders":true,"createdDate":"2025-12-09T19:32:35.204Z","lastModifiedDate":"2025-12-09T12:37:33.065Z","createdBy":"ha","lastModifiedBy":"oof gratefully ew","isDeleted":true},
    }).then(({ body }) => {
      userSettings = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/user-lites+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-lites').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-lites/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-profiles', {
      statusCode: 200,
      body: [userProfile],
    });

    cy.intercept('GET', '/api/user-settings', {
      statusCode: 200,
      body: [userSettings],
    });

    cy.intercept('GET', '/api/countries', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (userLite) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-lites/${userLite.id}`,
      }).then(() => {
        userLite = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (userProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-profiles/${userProfile.id}`,
      }).then(() => {
        userProfile = undefined;
      });
    }
    if (userSettings) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-settings/${userSettings.id}`,
      }).then(() => {
        userSettings = undefined;
      });
    }
  });
   */

  it('UserLites menu should load UserLites page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-lite');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserLite').should('exist');
    cy.url().should('match', userLitePageUrlPattern);
  });

  describe('UserLite page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userLitePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserLite page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-lite/new$'));
        cy.getEntityCreateUpdateHeading('UserLite');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-lites',
          body: {
            ...userLiteSample,
            profile: userProfile,
            settings: userSettings,
          },
        }).then(({ body }) => {
          userLite = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-lites+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-lites?page=0&size=20>; rel="last",<http://localhost/api/user-lites?page=0&size=20>; rel="first"',
              },
              body: [userLite],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userLitePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(userLitePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details UserLite page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userLite');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);
      });

      it('edit button click should load edit UserLite page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserLite');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);
      });

      it('edit button click should load edit UserLite page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserLite');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of UserLite', () => {
        cy.intercept('GET', '/api/user-lites/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userLite').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userLitePageUrlPattern);

        userLite = undefined;
      });
    });
  });

  describe('new UserLite page', () => {
    beforeEach(() => {
      cy.visit(`${userLitePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserLite');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of UserLite', () => {
      cy.get(`[data-cy="thumbnailS3Key"]`).type('vision joyfully');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'vision joyfully');

      cy.get(`[data-cy="birthDate"]`).type('2025-12-10');
      cy.get(`[data-cy="birthDate"]`).blur();
      cy.get(`[data-cy="birthDate"]`).should('have.value', '2025-12-10');

      cy.get(`[data-cy="gender"]`).select('MALE');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T13:13');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T13:13');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T12:48');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T12:48');

      cy.get(`[data-cy="createdBy"]`).type('athwart strictly');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'athwart strictly');

      cy.get(`[data-cy="lastModifiedBy"]`).type('given midst victoriously');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'given midst victoriously');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="nickName"]`).type('325iek-78jz77py');
      cy.get(`[data-cy="nickName"]`).should('have.value', '325iek-78jz77py');

      cy.get(`[data-cy="fullName"]`).type('4zPIb');
      cy.get(`[data-cy="fullName"]`).should('have.value', '4zPIb');

      cy.get(`[data-cy="profile"]`).select(1);
      cy.get(`[data-cy="settings"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userLite = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userLitePageUrlPattern);
    });
  });
});
