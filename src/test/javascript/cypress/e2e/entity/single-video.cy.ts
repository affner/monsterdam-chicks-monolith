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

describe('SingleVideo e2e test', () => {
  const singleVideoPageUrl = '/single-video';
  const singleVideoPageUrlPattern = new RegExp('/single-video(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const singleVideoSample = {
    thumbnailS3Key: 'after out not',
    contentS3Key: 'surprisingly meanwhile saw',
    createdDate: '2025-12-10T05:41:25.927Z',
    isDeleted: false,
    creatorId: 19914,
  };

  let singleVideo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/single-videos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/single-videos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/single-videos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (singleVideo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/single-videos/${singleVideo.id}`,
      }).then(() => {
        singleVideo = undefined;
      });
    }
  });

  it('SingleVideos menu should load SingleVideos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('single-video');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SingleVideo').should('exist');
    cy.url().should('match', singleVideoPageUrlPattern);
  });

  describe('SingleVideo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(singleVideoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SingleVideo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/single-video/new$'));
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/single-videos',
          body: singleVideoSample,
        }).then(({ body }) => {
          singleVideo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/single-videos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/single-videos?page=0&size=20>; rel="last",<http://localhost/api/single-videos?page=0&size=20>; rel="first"',
              },
              body: [singleVideo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(singleVideoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SingleVideo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('singleVideo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('edit button click should load edit SingleVideo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('edit button click should load edit SingleVideo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SingleVideo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);
      });

      it('last delete button click should delete instance of SingleVideo', () => {
        cy.intercept('GET', '/api/single-videos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('singleVideo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', singleVideoPageUrlPattern);

        singleVideo = undefined;
      });
    });
  });

  describe('new SingleVideo page', () => {
    beforeEach(() => {
      cy.visit(`${singleVideoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SingleVideo');
    });

    it('should create an instance of SingleVideo', () => {
      cy.get(`[data-cy="thumbnailS3Key"]`).type('because collaboration extremely');
      cy.get(`[data-cy="thumbnailS3Key"]`).should('have.value', 'because collaboration extremely');

      cy.get(`[data-cy="contentS3Key"]`).type('gladly');
      cy.get(`[data-cy="contentS3Key"]`).should('have.value', 'gladly');

      cy.get(`[data-cy="duration"]`).type('PT19M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT19M');

      cy.get(`[data-cy="likeCount"]`).type('19882');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '19882');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T09:37');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T09:37');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T07:05');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T07:05');

      cy.get(`[data-cy="createdBy"]`).type('instead phooey');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'instead phooey');

      cy.get(`[data-cy="lastModifiedBy"]`).type('serene deliberately plus');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'serene deliberately plus');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="creatorId"]`).type('2555');
      cy.get(`[data-cy="creatorId"]`).should('have.value', '2555');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        singleVideo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', singleVideoPageUrlPattern);
    });
  });
});
