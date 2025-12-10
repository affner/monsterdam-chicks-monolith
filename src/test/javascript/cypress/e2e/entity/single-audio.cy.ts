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
    thumbnailS3Key: 'considering',
    contentS3Key: 'mid',
    createdDate: '2025-12-09T16:02:15.865Z',
    isDeleted: false,
    creatorId: 30306,
  };

  let singleAudio;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/single-audios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-audios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-audios/*').as('deleteEntityRequest');
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
          body: singleAudioSample,
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
      cy.get(`[data-cy="thumbnailS3Key"]`).type('obligation until within');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'obligation until within');

      cy.get(`[data-cy="contentS3Key"]`).type('wetly besides');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'wetly besides');

      cy.get(`[data-cy="duration"]`).type('PT43M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT43M');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T00:59');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T00:59');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T07:43');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T07:43');

      cy.get(`[data-cy="createdBy"]`).type('gosh playfully');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'gosh playfully');

      cy.get(`[data-cy="lastModifiedBy"]`).type('soliloquy jump');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'soliloquy jump');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creatorId"]`).type('24192');
      cy.get(`[data-cy="creatorId"]`).should('have.value', '24192');

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
