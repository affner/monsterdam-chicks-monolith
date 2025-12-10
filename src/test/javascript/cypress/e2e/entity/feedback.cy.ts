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

describe('Feedback e2e test', () => {
  const feedbackPageUrl = '/feedback';
  const feedbackPageUrlPattern = new RegExp('/feedback(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const feedbackSample = {
    content: 'general',
    feedbackDate: '2025-12-10T01:08:57.784Z',
    createdDate: '2025-12-09T16:33:17.332Z',
    isDeleted: true,
  };

  let feedback;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/feedbacks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/feedbacks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/feedbacks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (feedback) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/feedbacks/${feedback.id}`,
      }).then(() => {
        feedback = undefined;
      });
    }
  });

  it('Feedbacks menu should load Feedbacks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('feedback');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Feedback').should('exist');
    cy.url().should('match', feedbackPageUrlPattern);
  });

  describe('Feedback page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(feedbackPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Feedback page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/feedback/new$'));
        cy.getEntityCreateUpdateHeading('Feedback');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/feedbacks',
          body: feedbackSample,
        }).then(({ body }) => {
          feedback = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/feedbacks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/feedbacks?page=0&size=20>; rel="last",<http://localhost/api/feedbacks?page=0&size=20>; rel="first"',
              },
              body: [feedback],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(feedbackPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Feedback page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('feedback');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);
      });

      it('edit button click should load edit Feedback page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Feedback');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);
      });

      it('edit button click should load edit Feedback page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Feedback');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);
      });

      it('last delete button click should delete instance of Feedback', () => {
        cy.intercept('GET', '/api/feedbacks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('feedback').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', feedbackPageUrlPattern);

        feedback = undefined;
      });
    });
  });

  describe('new Feedback page', () => {
    beforeEach(() => {
      cy.visit(`${feedbackPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Feedback');
    });

    it('should create an instance of Feedback', () => {
      cy.get(`[data-cy="content"]`).type('irritably how vivaciously');
      cy.get(`[data-cy="content"]`).should('have.value', 'irritably how vivaciously');

      cy.get(`[data-cy="feedbackDate"]`).type('2025-12-09T11:17');
      cy.get(`[data-cy="feedbackDate"]`).blur();
      cy.get(`[data-cy="feedbackDate"]`).should('have.value', '2025-12-09T11:17');

      cy.get(`[data-cy="feedbackRating"]`).type('22561');
      cy.get(`[data-cy="feedbackRating"]`).should('have.value', '22561');

      cy.get(`[data-cy="feedbackType"]`).select('ERROR');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-10T02:11');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-10T02:11');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-09T12:50');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-09T12:50');

      cy.get(`[data-cy="createdBy"]`).type('and what consequently');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'and what consequently');

      cy.get(`[data-cy="lastModifiedBy"]`).type('low quickly');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'low quickly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        feedback = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', feedbackPageUrlPattern);
    });
  });
});
