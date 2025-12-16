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

describe('LikeMark e2e test', () => {
  const likeMarkPageUrl = '/like-mark';
  const likeMarkPageUrlPattern = new RegExp('/like-mark(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const likeMarkSample = { entityType: 'STORY', createdDate: '2025-12-15T12:04:32.723Z' };

  let likeMark;
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
        thumbnailS3Key: 'boo',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-15T17:15:25.881Z',
        lastModifiedDate: '2025-12-15T15:17:44.135Z',
        createdBy: 'down angelic',
        lastModifiedBy: 'ponder provided glorious',
        deletedDate: '2025-12-15T20:05:41.264Z',
        nickName: '8ru',
        fullName: 'g1',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/like-marks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/like-marks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/like-marks/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (likeMark) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/like-marks/${likeMark.id}`,
      }).then(() => {
        likeMark = undefined;
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

  it('LikeMarks menu should load LikeMarks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('like-mark');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LikeMark').should('exist');
    cy.url().should('match', likeMarkPageUrlPattern);
  });

  describe('LikeMark page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(likeMarkPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LikeMark page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/like-mark/new$'));
        cy.getEntityCreateUpdateHeading('LikeMark');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/like-marks',
          body: {
            ...likeMarkSample,
            liker: userLite,
          },
        }).then(({ body }) => {
          likeMark = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/like-marks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/like-marks?page=0&size=20>; rel="last",<http://localhost/api/like-marks?page=0&size=20>; rel="first"',
              },
              body: [likeMark],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(likeMarkPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LikeMark page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('likeMark');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);
      });

      it('edit button click should load edit LikeMark page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LikeMark');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);
      });

      it('edit button click should load edit LikeMark page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LikeMark');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);
      });

      it('last delete button click should delete instance of LikeMark', () => {
        cy.intercept('GET', '/api/like-marks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('likeMark').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', likeMarkPageUrlPattern);

        likeMark = undefined;
      });
    });
  });

  describe('new LikeMark page', () => {
    beforeEach(() => {
      cy.visit(`${likeMarkPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LikeMark');
    });

    it('should create an instance of LikeMark', () => {
      cy.get(`[data-cy="entityType"]`).select('COMMENT');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-16T01:20');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-16T01:20');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T08:38');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T08:38');

      cy.get(`[data-cy="createdBy"]`).type('worth provided likely');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'worth provided likely');

      cy.get(`[data-cy="lastModifiedBy"]`).type('tall');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'tall');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T07:46');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T07:46');

      cy.get(`[data-cy="multimediaId"]`).type('27333');
      cy.get(`[data-cy="multimediaId"]`).should('have.value', '27333');

      cy.get(`[data-cy="messageId"]`).type('3661');
      cy.get(`[data-cy="messageId"]`).should('have.value', '3661');

      cy.get(`[data-cy="postId"]`).type('18756');
      cy.get(`[data-cy="postId"]`).should('have.value', '18756');

      cy.get(`[data-cy="commentId"]`).type('15254');
      cy.get(`[data-cy="commentId"]`).should('have.value', '15254');

      cy.get(`[data-cy="liker"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        likeMark = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', likeMarkPageUrlPattern);
    });
  });
});
