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

describe('PostPoll e2e test', () => {
  const postPollPageUrl = '/post-poll';
  const postPollPageUrlPattern = new RegExp('/post-poll(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const postPollSample = {
    question: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    isMultiChoice: true,
    endDate: '2025-12-09',
    duration: 6457,
    createdDate: '2025-12-09T10:20:45.495Z',
    isDeleted: true,
    postId: 31889,
  };

  let postPoll;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/post-polls+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-polls').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-polls/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (postPoll) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-polls/${postPoll.id}`,
      }).then(() => {
        postPoll = undefined;
      });
    }
  });

  it('PostPolls menu should load PostPolls page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-poll');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostPoll').should('exist');
    cy.url().should('match', postPollPageUrlPattern);
  });

  describe('PostPoll page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postPollPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostPoll page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-poll/new$'));
        cy.getEntityCreateUpdateHeading('PostPoll');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-polls',
          body: postPollSample,
        }).then(({ body }) => {
          postPoll = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-polls+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/post-polls?page=0&size=20>; rel="last",<http://localhost/api/post-polls?page=0&size=20>; rel="first"',
              },
              body: [postPoll],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(postPollPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PostPoll page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postPoll');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);
      });

      it('edit button click should load edit PostPoll page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostPoll');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);
      });

      it('edit button click should load edit PostPoll page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostPoll');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);
      });

      it('last delete button click should delete instance of PostPoll', () => {
        cy.intercept('GET', '/api/post-polls/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postPoll').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postPollPageUrlPattern);

        postPoll = undefined;
      });
    });
  });

  describe('new PostPoll page', () => {
    beforeEach(() => {
      cy.visit(`${postPollPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostPoll');
    });

    it('should create an instance of PostPoll', () => {
      cy.get(`[data-cy="question"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="question"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isMultiChoice"]`).should('not.be.checked');
      cy.get(`[data-cy="isMultiChoice"]`).click();
      cy.get(`[data-cy="isMultiChoice"]`).should('be.checked');

      cy.get(`[data-cy="endDate"]`).type('2025-12-10');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-12-10');

      cy.get(`[data-cy="duration"]`).type('PT14M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT14M');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T20:33');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T20:33');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T03:30');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T03:30');

      cy.get(`[data-cy="createdBy"]`).type('following per');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'following per');

      cy.get(`[data-cy="lastModifiedBy"]`).type('noon');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'noon');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="postId"]`).type('9956');
      cy.get(`[data-cy="postId"]`).should('have.value', '9956');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        postPoll = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', postPollPageUrlPattern);
    });
  });
});
