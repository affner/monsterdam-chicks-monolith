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

describe('HashTag e2e test', () => {
  const hashTagPageUrl = '/hash-tag';
  const hashTagPageUrlPattern = new RegExp('/hash-tag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const hashTagSample = { tagName: 'yawn besides', hashtagType: 'USER', createdDate: '2025-12-15T09:49:39.850Z' };

  let hashTag;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/hash-tags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/hash-tags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/hash-tags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (hashTag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/hash-tags/${hashTag.id}`,
      }).then(() => {
        hashTag = undefined;
      });
    }
  });

  it('HashTags menu should load HashTags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('hash-tag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HashTag').should('exist');
    cy.url().should('match', hashTagPageUrlPattern);
  });

  describe('HashTag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(hashTagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HashTag page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/hash-tag/new$'));
        cy.getEntityCreateUpdateHeading('HashTag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/hash-tags',
          body: hashTagSample,
        }).then(({ body }) => {
          hashTag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/hash-tags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/hash-tags?page=0&size=20>; rel="last",<http://localhost/api/hash-tags?page=0&size=20>; rel="first"',
              },
              body: [hashTag],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(hashTagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HashTag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('hashTag');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);
      });

      it('edit button click should load edit HashTag page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HashTag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);
      });

      it('edit button click should load edit HashTag page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HashTag');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);
      });

      it('last delete button click should delete instance of HashTag', () => {
        cy.intercept('GET', '/api/hash-tags/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('hashTag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', hashTagPageUrlPattern);

        hashTag = undefined;
      });
    });
  });

  describe('new HashTag page', () => {
    beforeEach(() => {
      cy.visit(`${hashTagPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HashTag');
    });

    it('should create an instance of HashTag', () => {
      cy.get(`[data-cy="tagName"]`).type('vague as yippee');
      cy.get(`[data-cy="tagName"]`).should('have.value', 'vague as yippee');

      cy.get(`[data-cy="hashtagType"]`).select('USER');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-16T04:57');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-16T04:57');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-16T05:07');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-16T05:07');

      cy.get(`[data-cy="createdBy"]`).type('hm');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'hm');

      cy.get(`[data-cy="lastModifiedBy"]`).type('rigidly oof upon');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'rigidly oof upon');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-16T04:29');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-16T04:29');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        hashTag = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', hashTagPageUrlPattern);
    });
  });
});
