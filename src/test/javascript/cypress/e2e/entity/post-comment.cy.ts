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
  const postCommentSample = {
    commentContent: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    createdDate: '2025-12-09T20:24:42.480Z',
    isDeleted: false,
    commenterId: 11985,
  };

  let postComment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/post-comments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-comments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-comments/*').as('deleteEntityRequest');
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
          body: postCommentSample,
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

      cy.get(`[data-cy="likeCount"]`).type('7919');
      cy.get(`[data-cy="likeCount"]`).should('have.value', '7919');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T10:49');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T10:49');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T06:34');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T06:34');

      cy.get(`[data-cy="createdBy"]`).type('drat geez');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'drat geez');

      cy.get(`[data-cy="lastModifiedBy"]`).type('so overconfidently whoever');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'so overconfidently whoever');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="commenterId"]`).type('63');
      cy.get(`[data-cy="commenterId"]`).should('have.value', '63');

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
