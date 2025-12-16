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

describe('PollVote e2e test', () => {
  const pollVotePageUrl = '/poll-vote';
  const pollVotePageUrlPattern = new RegExp('/poll-vote(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pollVoteSample = { createdDate: '2025-12-16T04:35:13.546Z' };

  let pollVote;
  let pollOption;
  let userLite;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/poll-options',
      body: { optionDescription: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' },
    }).then(({ body }) => {
      pollOption = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/user-lites',
      body: {
        thumbnailS3Key: 'yippee very',
        birthDate: '2025-12-16',
        gender: 'FEMALE',
        createdDate: '2025-12-15T12:33:26.522Z',
        lastModifiedDate: '2025-12-15T08:15:05.481Z',
        createdBy: 'mediocre',
        lastModifiedBy: 'jubilantly woot',
        deletedDate: '2025-12-16T01:06:21.754Z',
        nickName: 'lab5ia8j079',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/poll-votes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/poll-votes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/poll-votes/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/poll-options', {
      statusCode: 200,
      body: [pollOption],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (pollVote) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/poll-votes/${pollVote.id}`,
      }).then(() => {
        pollVote = undefined;
      });
    }
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
    if (userLite) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-lites/${userLite.id}`,
      }).then(() => {
        userLite = undefined;
      });
    }
  });

  it('PollVotes menu should load PollVotes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('poll-vote');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PollVote').should('exist');
    cy.url().should('match', pollVotePageUrlPattern);
  });

  describe('PollVote page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pollVotePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PollVote page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/poll-vote/new$'));
        cy.getEntityCreateUpdateHeading('PollVote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollVotePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/poll-votes',
          body: {
            ...pollVoteSample,
            pollOption,
            voter: userLite,
          },
        }).then(({ body }) => {
          pollVote = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/poll-votes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/poll-votes?page=0&size=20>; rel="last",<http://localhost/api/poll-votes?page=0&size=20>; rel="first"',
              },
              body: [pollVote],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pollVotePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PollVote page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pollVote');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollVotePageUrlPattern);
      });

      it('edit button click should load edit PollVote page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PollVote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollVotePageUrlPattern);
      });

      it('edit button click should load edit PollVote page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PollVote');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollVotePageUrlPattern);
      });

      it('last delete button click should delete instance of PollVote', () => {
        cy.intercept('GET', '/api/poll-votes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('pollVote').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pollVotePageUrlPattern);

        pollVote = undefined;
      });
    });
  });

  describe('new PollVote page', () => {
    beforeEach(() => {
      cy.visit(`${pollVotePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PollVote');
    });

    it('should create an instance of PollVote', () => {
      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T07:22');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T07:22');

      cy.get(`[data-cy="pollOption"]`).select(1);
      cy.get(`[data-cy="voter"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pollVote = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', pollVotePageUrlPattern);
    });
  });
});
