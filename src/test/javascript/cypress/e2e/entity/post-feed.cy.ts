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

describe('PostFeed e2e test', () => {
  const postFeedPageUrl = '/post-feed';
  const postFeedPageUrlPattern = new RegExp('/post-feed(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const postFeedSample = { postContent: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', createdDate: '2025-12-15T13:13:43.510Z' };

  let postFeed;
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
        thumbnailS3Key: 'until phew kinase',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-15T22:56:29.108Z',
        lastModifiedDate: '2025-12-15T07:00:29.762Z',
        createdBy: 'spanish',
        lastModifiedBy: 'how inasmuch closed',
        deletedDate: '2025-12-15T22:33:08.089Z',
        nickName: 'ezxai3',
        fullName: 'b',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/post-feeds+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-feeds').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-feeds/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });

    cy.intercept('GET', '/api/post-polls', {
      statusCode: 200,
      body: [],
    });
  });

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

  it('PostFeeds menu should load PostFeeds page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-feed');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostFeed').should('exist');
    cy.url().should('match', postFeedPageUrlPattern);
  });

  describe('PostFeed page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postFeedPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostFeed page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-feed/new$'));
        cy.getEntityCreateUpdateHeading('PostFeed');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-feeds',
          body: {
            ...postFeedSample,
            creator: userLite,
          },
        }).then(({ body }) => {
          postFeed = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-feeds+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/post-feeds?page=0&size=20>; rel="last",<http://localhost/api/post-feeds?page=0&size=20>; rel="first"',
              },
              body: [postFeed],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(postFeedPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PostFeed page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postFeed');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);
      });

      it('edit button click should load edit PostFeed page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostFeed');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);
      });

      it('edit button click should load edit PostFeed page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostFeed');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);
      });

      it('last delete button click should delete instance of PostFeed', () => {
        cy.intercept('GET', '/api/post-feeds/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postFeed').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postFeedPageUrlPattern);

        postFeed = undefined;
      });
    });
  });

  describe('new PostFeed page', () => {
    beforeEach(() => {
      cy.visit(`${postFeedPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostFeed');
    });

    it('should create an instance of PostFeed', () => {
      cy.get(`[data-cy="postContent"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="postContent"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isPublic"]`).should('not.be.checked');
      cy.get(`[data-cy="isPublic"]`).click();
      cy.get(`[data-cy="isPublic"]`).should('be.checked');

      cy.get(`[data-cy="pinnedPost"]`).should('not.be.checked');
      cy.get(`[data-cy="pinnedPost"]`).click();
      cy.get(`[data-cy="pinnedPost"]`).should('be.checked');

      cy.get(`[data-cy="likeCount"]`).type('9614');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '9614');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-16T02:25');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-16T02:25');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T21:54');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T21:54');

      cy.get(`[data-cy="createdBy"]`).type('sympathetically');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'sympathetically');

      cy.get(`[data-cy="lastModifiedBy"]`).type('afore furiously fencing');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'afore furiously fencing');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T23:07');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T23:07');

      cy.get(`[data-cy="creator"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        postFeed = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', postFeedPageUrlPattern);
    });
  });
});
