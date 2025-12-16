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

describe('PlatformAdminUser e2e test', () => {
  const platformAdminUserPageUrl = '/platform-admin-user';
  const platformAdminUserPageUrlPattern = new RegExp('/platform-admin-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const platformAdminUserSample = {
    fullName: 'blossom atop when',
    emailAddress: 'and',
    gender: 'MALE',
    birthDate: '2025-12-16',
    createdDate: '2025-12-15T12:17:46.488Z',
  };

  let platformAdminUser;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/platform-admin-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/platform-admin-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/platform-admin-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (platformAdminUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/platform-admin-users/${platformAdminUser.id}`,
      }).then(() => {
        platformAdminUser = undefined;
      });
    }
  });

  it('PlatformAdminUsers menu should load PlatformAdminUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('platform-admin-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PlatformAdminUser').should('exist');
    cy.url().should('match', platformAdminUserPageUrlPattern);
  });

  describe('PlatformAdminUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(platformAdminUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PlatformAdminUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/platform-admin-user/new$'));
        cy.getEntityCreateUpdateHeading('PlatformAdminUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', platformAdminUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/platform-admin-users',
          body: platformAdminUserSample,
        }).then(({ body }) => {
          platformAdminUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/platform-admin-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/platform-admin-users?page=0&size=20>; rel="last",<http://localhost/api/platform-admin-users?page=0&size=20>; rel="first"',
              },
              body: [platformAdminUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(platformAdminUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PlatformAdminUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('platformAdminUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', platformAdminUserPageUrlPattern);
      });

      it('edit button click should load edit PlatformAdminUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlatformAdminUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', platformAdminUserPageUrlPattern);
      });

      it('edit button click should load edit PlatformAdminUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlatformAdminUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', platformAdminUserPageUrlPattern);
      });

      it('last delete button click should delete instance of PlatformAdminUser', () => {
        cy.intercept('GET', '/api/platform-admin-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('platformAdminUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', platformAdminUserPageUrlPattern);

        platformAdminUser = undefined;
      });
    });
  });

  describe('new PlatformAdminUser page', () => {
    beforeEach(() => {
      cy.visit(`${platformAdminUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PlatformAdminUser');
    });

    it('should create an instance of PlatformAdminUser', () => {
      cy.get(`[data-cy="fullName"]`).type('sentimental metallic circumference');
      cy.get(`[data-cy="fullName"]`).should('have.value', 'sentimental metallic circumference');

      cy.get(`[data-cy="emailAddress"]`).type('unexpectedly rudely costume');
      cy.get(`[data-cy="emailAddress"]`).should('have.value', 'unexpectedly rudely costume');

      cy.get(`[data-cy="nickName"]`).type('terribly');
      cy.get(`[data-cy="nickName"]`).should('have.value', 'terribly');

      cy.get(`[data-cy="gender"]`).select('MALE');

      cy.get(`[data-cy="mobilePhone"]`).type('zowie');
      cy.get(`[data-cy="mobilePhone"]`).should('have.value', 'zowie');

      cy.get(`[data-cy="lastLoginDate"]`).type('2025-12-16T01:24');
      cy.get(`[data-cy="lastLoginDate"]`).blur();
      cy.get(`[data-cy="lastLoginDate"]`).should('have.value', '2025-12-16T01:24');

      cy.get(`[data-cy="birthDate"]`).type('2025-12-15');
      cy.get(`[data-cy="birthDate"]`).blur();
      cy.get(`[data-cy="birthDate"]`).should('have.value', '2025-12-15');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-16T04:42');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-16T04:42');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T21:29');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T21:29');

      cy.get(`[data-cy="createdBy"]`).type('why');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'why');

      cy.get(`[data-cy="lastModifiedBy"]`).type('ick nice');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'ick nice');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T22:34');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T22:34');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        platformAdminUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', platformAdminUserPageUrlPattern);
    });
  });
});
