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

describe('PurchasedContent e2e test', () => {
  const purchasedContentPageUrl = '/purchased-content';
  const purchasedContentPageUrlPattern = new RegExp('/purchased-content(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const purchasedContentSample = { createdDate: '2025-12-16T02:31:00.164Z' };

  let purchasedContent;
  let contentPackage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/content-packages',
      body: {
        amount: 31093.24,
        videoCount: 21724,
        imageCount: 21155,
        isPaidContent: false,
        createdDate: '2025-12-15T22:37:32.823Z',
        lastModifiedDate: '2025-12-16T02:36:47.229Z',
        createdBy: 'trustworthy refer',
        lastModifiedBy: 'other tough',
        deletedDate: '2025-12-15T21:44:01.366Z',
        messageId: 15344,
        postId: 9545,
      },
    }).then(({ body }) => {
      contentPackage = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/purchased-contents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/purchased-contents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/purchased-contents/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/content-packages', {
      statusCode: 200,
      body: [contentPackage],
    });

    cy.intercept('GET', '/api/payments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/user-lites', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (purchasedContent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/purchased-contents/${purchasedContent.id}`,
      }).then(() => {
        purchasedContent = undefined;
      });
    }
  });

  afterEach(() => {
    if (contentPackage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/content-packages/${contentPackage.id}`,
      }).then(() => {
        contentPackage = undefined;
      });
    }
  });

  it('PurchasedContents menu should load PurchasedContents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('purchased-content');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PurchasedContent').should('exist');
    cy.url().should('match', purchasedContentPageUrlPattern);
  });

  describe('PurchasedContent page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(purchasedContentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PurchasedContent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/purchased-content/new$'));
        cy.getEntityCreateUpdateHeading('PurchasedContent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/purchased-contents',
          body: {
            ...purchasedContentSample,
            contentPackage,
          },
        }).then(({ body }) => {
          purchasedContent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/purchased-contents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/purchased-contents?page=0&size=20>; rel="last",<http://localhost/api/purchased-contents?page=0&size=20>; rel="first"',
              },
              body: [purchasedContent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(purchasedContentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PurchasedContent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('purchasedContent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);
      });

      it('edit button click should load edit PurchasedContent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedContent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);
      });

      it('edit button click should load edit PurchasedContent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PurchasedContent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);
      });

      it('last delete button click should delete instance of PurchasedContent', () => {
        cy.intercept('GET', '/api/purchased-contents/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('purchasedContent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', purchasedContentPageUrlPattern);

        purchasedContent = undefined;
      });
    });
  });

  describe('new PurchasedContent page', () => {
    beforeEach(() => {
      cy.visit(`${purchasedContentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PurchasedContent');
    });

    it('should create an instance of PurchasedContent', () => {
      cy.get(`[data-cy="rating"]`).type('13213.94');
      cy.get(`[data-cy="rating"]`).should('have.value', '13213.94');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T18:49');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T18:49');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-16T00:48');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-16T00:48');

      cy.get(`[data-cy="createdBy"]`).type('woefully skean');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'woefully skean');

      cy.get(`[data-cy="lastModifiedBy"]`).type('impish');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'impish');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-16T03:07');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-16T03:07');

      cy.get(`[data-cy="contentPackage"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        purchasedContent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', purchasedContentPageUrlPattern);
    });
  });
});
