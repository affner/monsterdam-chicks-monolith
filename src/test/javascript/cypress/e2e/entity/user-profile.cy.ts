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

describe('UserProfile e2e test', () => {
  const userProfilePageUrl = '/user-profile';
  const userProfilePageUrlPattern = new RegExp('/user-profile(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userProfileSample = { emailContact: 'teeming', lastLoginDate: '2025-12-15T19:02:01.777Z', createdDate: '2025-12-16T02:34:17.089Z' };

  let userProfile;
  let userLite;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-lites',
      body: {
        thumbnailS3Key: 'executor fooey yum',
        birthDate: '2025-12-16',
        gender: 'MALE',
        createdDate: '2025-12-15T22:41:50.611Z',
        lastModifiedDate: '2025-12-15T07:33:06.820Z',
        createdBy: 'aboard ew',
        lastModifiedBy: 'after gadzooks republican',
        deletedDate: '2025-12-15T17:26:58.610Z',
        nickName: 'trmdx',
        fullName: 'Lr',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-profiles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-profiles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-profiles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/states', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (userProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-profiles/${userProfile.id}`,
      }).then(() => {
        userProfile = undefined;
      });
    }
  });

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

  it('UserProfiles menu should load UserProfiles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-profile');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserProfile').should('exist');
    cy.url().should('match', userProfilePageUrlPattern);
  });

  describe('UserProfile page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userProfilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserProfile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-profile/new$'));
        cy.getEntityCreateUpdateHeading('UserProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-profiles',
          body: {
            ...userProfileSample,
            user: userLite,
          },
        }).then(({ body }) => {
          userProfile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-profiles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-profiles?page=0&size=20>; rel="last",<http://localhost/api/user-profiles?page=0&size=20>; rel="first"',
              },
              body: [userProfile],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userProfilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserProfile page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userProfile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);
      });

      it('edit button click should load edit UserProfile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);
      });

      it('edit button click should load edit UserProfile page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserProfile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);
      });

      it('last delete button click should delete instance of UserProfile', () => {
        cy.intercept('GET', '/api/user-profiles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userProfile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfilePageUrlPattern);

        userProfile = undefined;
      });
    });
  });

  describe('new UserProfile page', () => {
    beforeEach(() => {
      cy.visit(`${userProfilePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserProfile');
    });

    it('should create an instance of UserProfile', () => {
      cy.get(`[data-cy="emailContact"]`).type('mostly');
      cy.get(`[data-cy="emailContact"]`).should('have.value', 'mostly');

      cy.get(`[data-cy="profilePhotoS3Key"]`).type('tattered');
      cy.get(`[data-cy="profilePhotoS3Key"]`).should('have.value', 'tattered');

      cy.get(`[data-cy="coverPhotoS3Key"]`).type('membership strictly overdue');
      cy.get(`[data-cy="coverPhotoS3Key"]`).should('have.value', 'membership strictly overdue');

      cy.get(`[data-cy="mainContentUrl"]`).type('bore');
      cy.get(`[data-cy="mainContentUrl"]`).should('have.value', 'bore');

      cy.get(`[data-cy="mobilePhone"]`).type('forgo reopen');
      cy.get(`[data-cy="mobilePhone"]`).should('have.value', 'forgo reopen');

      cy.get(`[data-cy="websiteUrl"]`).type('charming tuxedo');
      cy.get(`[data-cy="websiteUrl"]`).should('have.value', 'charming tuxedo');

      cy.get(`[data-cy="amazonWishlistUrl"]`).type('worth to');
      cy.get(`[data-cy="amazonWishlistUrl"]`).should('have.value', 'worth to');

      cy.get(`[data-cy="lastLoginDate"]`).type('2025-12-16T05:17');
      cy.get(`[data-cy="lastLoginDate"]`).blur();
      cy.get(`[data-cy="lastLoginDate"]`).should('have.value', '2025-12-16T05:17');

      cy.get(`[data-cy="biography"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="biography"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isFree"]`).should('not.be.checked');
      cy.get(`[data-cy="isFree"]`).click();
      cy.get(`[data-cy="isFree"]`).should('be.checked');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T09:46');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T09:46');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-16T04:14');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-16T04:14');

      cy.get(`[data-cy="createdBy"]`).type('eek hmph arcade');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'eek hmph arcade');

      cy.get(`[data-cy="lastModifiedBy"]`).type('next pharmacopoeia loyalty');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'next pharmacopoeia loyalty');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T22:37');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T22:37');

      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userProfile = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userProfilePageUrlPattern);
    });
  });
});
