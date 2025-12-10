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

describe('PollOption e2e test', () => {
  const pollOptionPageUrl = '/poll-option';
  const pollOptionPageUrlPattern = new RegExp('/poll-option(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pollOptionSample = { optionDescription: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', voteCount: 16832 };

  let pollOption;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/poll-options+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/poll-options').as('postEntityRequest');
    cy.intercept('DELETE', '/api/poll-options/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pollOption) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/poll-options/${pollOption.id}`,
      }).then(() => {
        pollOption = undefined;
      });
    }
  });

  it('PollOptions menu should load PollOptions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('poll-option');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PollOption').should('exist');
    cy.url().should('match', pollOptionPageUrlPattern);
  });

  describe('PollOption page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pollOptionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PollOption page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/poll-option/new$'));
        cy.getEntityCreateUpdateHeading('PollOption');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/poll-options',
          body: pollOptionSample,
        }).then(({ body }) => {
          pollOption = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/poll-options+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/poll-options?page=0&size=20>; rel="last",<http://localhost/api/poll-options?page=0&size=20>; rel="first"',
              },
              body: [pollOption],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pollOptionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PollOption page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pollOption');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);
      });

      it('edit button click should load edit PollOption page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PollOption');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);
      });

      it('edit button click should load edit PollOption page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PollOption');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);
      });

      it('last delete button click should delete instance of PollOption', () => {
        cy.intercept('GET', '/api/poll-options/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('pollOption').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollOptionPageUrlPattern);

        pollOption = undefined;
      });
    });
  });

  describe('new PollOption page', () => {
    beforeEach(() => {
      cy.visit(`${pollOptionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PollOption');
    });

    it('should create an instance of PollOption', () => {
      cy.get(`[data-cy="optionDescription"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="optionDescription"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="voteCount"]`).type('8409');
      cy.get(`[data-cy="voteCount"]`).should('have.value', '8409');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pollOption = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', pollOptionPageUrlPattern);
    });
  });
});
