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

describe('IdentityDocumentReview e2e test', () => {
  const identityDocumentReviewPageUrl = '/identity-document-review';
  const identityDocumentReviewPageUrlPattern = new RegExp('/identity-document-review(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const identityDocumentReviewSample = { createdDate: '2025-12-15T08:10:14.175Z' };

  let identityDocumentReview;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/identity-document-reviews+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/identity-document-reviews').as('postEntityRequest');
    cy.intercept('DELETE', '/api/identity-document-reviews/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (identityDocumentReview) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/identity-document-reviews/${identityDocumentReview.id}`,
      }).then(() => {
        identityDocumentReview = undefined;
      });
    }
  });

  it('IdentityDocumentReviews menu should load IdentityDocumentReviews page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('identity-document-review');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IdentityDocumentReview').should('exist');
    cy.url().should('match', identityDocumentReviewPageUrlPattern);
  });

  describe('IdentityDocumentReview page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(identityDocumentReviewPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IdentityDocumentReview page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/identity-document-review/new$'));
        cy.getEntityCreateUpdateHeading('IdentityDocumentReview');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/identity-document-reviews',
          body: identityDocumentReviewSample,
        }).then(({ body }) => {
          identityDocumentReview = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/identity-document-reviews+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/identity-document-reviews?page=0&size=20>; rel="last",<http://localhost/api/identity-document-reviews?page=0&size=20>; rel="first"',
              },
              body: [identityDocumentReview],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(identityDocumentReviewPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IdentityDocumentReview page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('identityDocumentReview');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);
      });

      it('edit button click should load edit IdentityDocumentReview page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocumentReview');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);
      });

      it('edit button click should load edit IdentityDocumentReview page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocumentReview');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);
      });

      it('last delete button click should delete instance of IdentityDocumentReview', () => {
        cy.intercept('GET', '/api/identity-document-reviews/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('identityDocumentReview').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', identityDocumentReviewPageUrlPattern);

        identityDocumentReview = undefined;
      });
    });
  });

  describe('new IdentityDocumentReview page', () => {
    beforeEach(() => {
      cy.visit(`${identityDocumentReviewPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IdentityDocumentReview');
    });

    it('should create an instance of IdentityDocumentReview', () => {
      cy.get(`[data-cy="documentStatus"]`).select('APPROVED');

      cy.get(`[data-cy="resolutionDate"]`).type('2025-12-16T00:20');
      cy.get(`[data-cy="resolutionDate"]`).blur();
      cy.get(`[data-cy="resolutionDate"]`).should('have.value', '2025-12-16T00:20');

      cy.get(`[data-cy="reviewStatus"]`).select('REVIEWING');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T23:17');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T23:17');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T21:14');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T21:14');

      cy.get(`[data-cy="createdBy"]`).type('during mostly ugh');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'during mostly ugh');

      cy.get(`[data-cy="lastModifiedBy"]`).type('optimal yet');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'optimal yet');

      cy.get(`[data-cy="ticketId"]`).type('20700');
      cy.get(`[data-cy="ticketId"]`).should('have.value', '20700');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        identityDocumentReview = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', identityDocumentReviewPageUrlPattern);
    });
  });
});
