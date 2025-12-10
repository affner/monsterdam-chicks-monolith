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

describe('CommentMention e2e test', () => {
  const commentMentionPageUrl = '/comment-mention';
  const commentMentionPageUrlPattern = new RegExp('/comment-mention(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const commentMentionSample = { createdDate: '2025-12-09T17:58:58.919Z', isDeleted: true, mentionedUserId: 14492 };

  let commentMention;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/comment-mentions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/comment-mentions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/comment-mentions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (commentMention) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/comment-mentions/${commentMention.id}`,
      }).then(() => {
        commentMention = undefined;
      });
    }
  });

  it('CommentMentions menu should load CommentMentions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('comment-mention');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CommentMention').should('exist');
    cy.url().should('match', commentMentionPageUrlPattern);
  });

  describe('CommentMention page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(commentMentionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CommentMention page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/comment-mention/new$'));
        cy.getEntityCreateUpdateHeading('CommentMention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', commentMentionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/comment-mentions',
          body: commentMentionSample,
        }).then(({ body }) => {
          commentMention = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/comment-mentions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/comment-mentions?page=0&size=20>; rel="last",<http://localhost/api/comment-mentions?page=0&size=20>; rel="first"',
              },
              body: [commentMention],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(commentMentionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CommentMention page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('commentMention');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', commentMentionPageUrlPattern);
      });

      it('edit button click should load edit CommentMention page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommentMention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', commentMentionPageUrlPattern);
      });

      it('edit button click should load edit CommentMention page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommentMention');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', commentMentionPageUrlPattern);
      });

      it('last delete button click should delete instance of CommentMention', () => {
        cy.intercept('GET', '/api/comment-mentions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('commentMention').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', commentMentionPageUrlPattern);

        commentMention = undefined;
      });
    });
  });

  describe('new CommentMention page', () => {
    beforeEach(() => {
      cy.visit(`${commentMentionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CommentMention');
    });

    it('should create an instance of CommentMention', () => {
      cy.get(`[data-cy="createdDate"]`).type('2025-12-09T11:55');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-09T11:55');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-10T03:22');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-10T03:22');

      cy.get(`[data-cy="createdBy"]`).type('ack');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'ack');

      cy.get(`[data-cy="lastModifiedBy"]`).type('yippee until');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'yippee until');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="mentionedUserId"]`).type('27011');
      cy.get(`[data-cy="mentionedUserId"]`).should('have.value', '27011');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        commentMention = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', commentMentionPageUrlPattern);
    });
  });
});
