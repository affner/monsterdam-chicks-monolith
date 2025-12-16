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

describe('PostMention e2e test', () => {
  const postMentionPageUrl = '/post-mention';
  const postMentionPageUrlPattern = new RegExp('/post-mention(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const postMentionSample = { createdDate: '2025-12-15T13:08:11.432Z' };

  let postMention;
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
        thumbnailS3Key: 'fellow round',
        birthDate: '2025-12-15',
        gender: 'FEMALE',
        createdDate: '2025-12-16T05:55:34.363Z',
        lastModifiedDate: '2025-12-15T23:52:38.959Z',
        createdBy: 'glossy bookend',
        lastModifiedBy: 'gown',
        deletedDate: '2025-12-15T14:42:33.653Z',
        nickName: 'h92z31-xhioiw',
        fullName: 'S7Y',
      },
    }).then(({ body }) => {
      userLite = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/post-mentions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-mentions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-mentions/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/post-feeds', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [userLite],
    });
  });

  afterEach(() => {
    if (postMention) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-mentions/${postMention.id}`,
      }).then(() => {
        postMention = undefined;
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

  it('PostMentions menu should load PostMentions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-mention');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostMention').should('exist');
    cy.url().should('match', postMentionPageUrlPattern);
  });

  describe('PostMention page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postMentionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostMention page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-mention/new$'));
        cy.getEntityCreateUpdateHeading('PostMention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postMentionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-mentions',
          body: {
            ...postMentionSample,
            mentionedUser: userLite,
          },
        }).then(({ body }) => {
          postMention = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-mentions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/post-mentions?page=0&size=20>; rel="last",<http://localhost/api/post-mentions?page=0&size=20>; rel="first"',
              },
              body: [postMention],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(postMentionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PostMention page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postMention');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postMentionPageUrlPattern);
      });

      it('edit button click should load edit PostMention page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostMention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postMentionPageUrlPattern);
      });

      it('edit button click should load edit PostMention page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostMention');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postMentionPageUrlPattern);
      });

      it('last delete button click should delete instance of PostMention', () => {
        cy.intercept('GET', '/api/post-mentions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postMention').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postMentionPageUrlPattern);

        postMention = undefined;
      });
    });
  });

  describe('new PostMention page', () => {
    beforeEach(() => {
      cy.visit(`${postMentionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostMention');
    });

    it('should create an instance of PostMention', () => {
      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T12:36');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T12:36');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T23:10');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T23:10');

      cy.get(`[data-cy="createdBy"]`).type('known');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'known');

      cy.get(`[data-cy="lastModifiedBy"]`).type('although past');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'although past');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T13:05');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T13:05');

      cy.get(`[data-cy="mentionedUser"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        postMention = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', postMentionPageUrlPattern);
    });
  });
});
