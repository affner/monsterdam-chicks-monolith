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
  const commentMentionSample = { createdDate: '2025-12-15T17:28:59.807Z' };

  let commentMention;
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
        thumbnailS3Key: 'yippee override indeed',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-15T16:03:57.774Z',
        lastModifiedDate: '2025-12-15T10:28:58.894Z',
        createdBy: 'pace',
        lastModifiedBy: 'whose',
        deletedDate: '2025-12-16T02:03:28.752Z',
        nickName: 'h81q7glcparbm7v5',
        fullName: 'F4',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/comment-mentions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/comment-mentions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/comment-mentions/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
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
    if (commentMention) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/comment-mentions/${commentMention.id}`,
      }).then(() => {
        commentMention = undefined;
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
          body: {
            ...commentMentionSample,
            mentionedUser: userLite,
          },
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
      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T14:37');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T14:37');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T08:36');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T08:36');

      cy.get(`[data-cy="createdBy"]`).type('throughout dearly upward');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'throughout dearly upward');

      cy.get(`[data-cy="lastModifiedBy"]`).type('for towards playfully');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'for towards playfully');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T18:55');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T18:55');

      cy.get(`[data-cy="mentionedUser"]`).select(1);

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
