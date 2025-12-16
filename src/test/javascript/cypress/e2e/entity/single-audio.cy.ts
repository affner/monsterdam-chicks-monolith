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

describe('SingleAudio e2e test', () => {
  const singleAudioPageUrl = '/single-audio';
  const singleAudioPageUrlPattern = new RegExp('/single-audio(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const singleAudioSample = {
    thumbnail: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    thumbnailContentType: 'unknown',
    thumbnailS3Key: 'intensely hence',
    contentS3Key: 'amid',
    createdDate: '2025-12-16T05:23:54.204Z',
  };

  let singleAudio;
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
        thumbnailS3Key: 'to',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-15T17:49:21.243Z',
        lastModifiedDate: '2025-12-15T22:32:37.675Z',
        createdBy: 'ultimately absentmindedly pfft',
        lastModifiedBy: 'marimba',
        deletedDate: '2025-12-15T15:40:38.647Z',
        nickName: 'wqq1vyjs7',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/single-audios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-audios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-audios/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/content-packages', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (singleAudio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-audios/${singleAudio.id}`,
      }).then(() => {
        singleAudio = undefined;
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

  it('SingleAudios menu should load SingleAudios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-audio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SingleAudio').should('exist');
    cy.url().should('match', singleAudioPageUrlPattern);
  });

  describe('SingleAudio page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singleAudioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SingleAudio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-audio/new$'));
        cy.getEntityCreateUpdateHeading('SingleAudio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-audios',
          body: {
            ...singleAudioSample,
            creator: userLite,
          },
        }).then(({ body }) => {
          singleAudio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-audios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-audios?page=0&size=20>; rel="last",<http://localhost/api/single-audios?page=0&size=20>; rel="first"',
              },
              body: [singleAudio],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(singleAudioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SingleAudio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singleAudio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);
      });

      it('edit button click should load edit SingleAudio page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleAudio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);
      });

      it('edit button click should load edit SingleAudio page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleAudio');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);
      });

      it('last delete button click should delete instance of SingleAudio', () => {
        cy.intercept('GET', '/api/single-audios/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singleAudio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleAudioPageUrlPattern);

        singleAudio = undefined;
      });
    });
  });

  describe('new SingleAudio page', () => {
    beforeEach(() => {
      cy.visit(`${singleAudioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SingleAudio');
    });

    it('should create an instance of SingleAudio', () => {
      cy.setFieldImageAsBytesOfEntity('thumbnail', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="thumbnailS3Key"]`).type('round');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'round');

      cy.setFieldImageAsBytesOfEntity('content', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="contentS3Key"]`).type('serialize reel');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'serialize reel');

      cy.get(`[data-cy="duration"]`).type('PT54M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT54M');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-16T02:19');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-16T02:19');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-16T04:17');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-16T04:17');

      cy.get(`[data-cy="createdBy"]`).type('why');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'why');

      cy.get(`[data-cy="lastModifiedBy"]`).type('whole whispered wherever');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'whole whispered wherever');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T07:06');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T07:06');

      cy.get(`[data-cy="creator"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        singleAudio = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', singleAudioPageUrlPattern);
    });
  });
});
