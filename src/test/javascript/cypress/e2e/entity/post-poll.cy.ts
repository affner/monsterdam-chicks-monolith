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
  // const postPollSample = {"question":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isMultiChoice":true,"endDate":"2025-12-15","duration":24855,"createdDate":"2025-12-16T04:12:57.277Z"};

  let postPoll;
  // let postFeed;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/post-feeds',
      body: {"postContent":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","isHidden":false,"pinnedPost":true,"likeCount":22387,"createdDate":"2025-12-16T06:30:17.752Z","lastModifiedDate":"2025-12-15T18:44:50.601Z","createdBy":"wisely about quinoa","lastModifiedBy":"instead","deletedDate":"2025-12-15T21:19:00.663Z"},
    }).then(({ body }) => {
      postFeed = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/post-polls+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-polls').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-polls/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [postFeed],
    });

  });
   */

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

  /* Disabled due to incompatibility
  afterEach(() => {
    if (postFeed) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-feeds/${postFeed.id}`,
      }).then(() => {
        postFeed = undefined;
      });
    }
  });
   */

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
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-polls',
          body: {
            ...postPollSample,
            post: postFeed,
          },
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
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(postPollPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(postPollPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
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

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of PostPoll', () => {
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

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of PostPoll', () => {
      cy.get(`[data-cy="question"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="question"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isMultiChoice"]`).should('not.be.checked');
      cy.get(`[data-cy="isMultiChoice"]`).click();
      cy.get(`[data-cy="isMultiChoice"]`).should('be.checked');

      cy.get(`[data-cy="endDate"]`).type('2025-12-15');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2025-12-15');

      cy.get(`[data-cy="duration"]`).type('PT36M');
      cy.get(`[data-cy="duration"]`).blur();
      cy.get(`[data-cy="duration"]`).should('have.value', 'PT36M');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T12:34');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T12:34');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T17:13');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T17:13');

      cy.get(`[data-cy="createdBy"]`).type('icy armoire fooey');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'icy armoire fooey');

      cy.get(`[data-cy="lastModifiedBy"]`).type('accept doorpost');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'accept doorpost');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T16:52');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T16:52');

      cy.get(`[data-cy="post"]`).select(1);

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
