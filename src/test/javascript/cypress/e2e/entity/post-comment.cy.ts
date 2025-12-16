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

describe('PostComment e2e test', () => {
  const postCommentPageUrl = '/post-comment';
  const postCommentPageUrlPattern = new RegExp('/post-comment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const postCommentSample = { commentContent: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=', createdDate: '2025-12-15T23:17:07.659Z' };

  let postComment;
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
        thumbnailS3Key: 'versus chunder',
        birthDate: '2025-12-16',
        gender: 'FEMALE',
        createdDate: '2025-12-15T17:52:57.815Z',
        lastModifiedDate: '2025-12-15T20:11:12.199Z',
        createdBy: 'mmm',
        lastModifiedBy: 'buttery reservation',
        deletedDate: '2025-12-16T06:01:24.083Z',
        nickName: 'qru8h7nu6u0ny',
        fullName: 'p',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/post-comments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-comments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-comments/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-comments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (postComment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-comments/${postComment.id}`,
      }).then(() => {
        postComment = undefined;
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

  it('PostComments menu should load PostComments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-comment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostComment').should('exist');
    cy.url().should('match', postCommentPageUrlPattern);
  });

  describe('PostComment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postCommentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostComment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-comment/new$'));
        cy.getEntityCreateUpdateHeading('PostComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-comments',
          body: {
            ...postCommentSample,
            commenter: userLite,
          },
        }).then(({ body }) => {
          postComment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-comments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/post-comments?page=0&size=20>; rel="last",<http://localhost/api/post-comments?page=0&size=20>; rel="first"',
              },
              body: [postComment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(postCommentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PostComment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postComment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);
      });

      it('edit button click should load edit PostComment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);
      });

      it('edit button click should load edit PostComment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostComment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);
      });

      it('last delete button click should delete instance of PostComment', () => {
        cy.intercept('GET', '/api/post-comments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postComment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postCommentPageUrlPattern);

        postComment = undefined;
      });
    });
  });

  describe('new PostComment page', () => {
    beforeEach(() => {
      cy.visit(`${postCommentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostComment');
    });

    it('should create an instance of PostComment', () => {
      cy.get(`[data-cy="commentContent"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="commentContent"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="likeCount"]`).type('17895');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '17895');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T12:39');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T12:39');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T07:30');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T07:30');

      cy.get(`[data-cy="createdBy"]`).type('once accredit beyond');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'once accredit beyond');

      cy.get(`[data-cy="lastModifiedBy"]`).type('fussy');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'fussy');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T20:19');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T20:19');

      cy.get(`[data-cy="commenter"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        postComment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', postCommentPageUrlPattern);
    });
  });
});
