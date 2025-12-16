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

describe('ViewerWallet e2e test', () => {
  const viewerWalletPageUrl = '/viewer-wallet';
  const viewerWalletPageUrlPattern = new RegExp('/viewer-wallet(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const viewerWalletSample = { amount: 2666.12, currency: 'fel', transactionType: 'WITHDRAW', createdDate: '2025-12-15T20:54:41.549Z' };

  let viewerWallet;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/viewer-wallets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/viewer-wallets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/viewer-wallets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (viewerWallet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/viewer-wallets/${viewerWallet.id}`,
      }).then(() => {
        viewerWallet = undefined;
      });
    }
  });

  it('ViewerWallets menu should load ViewerWallets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('viewer-wallet');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ViewerWallet').should('exist');
    cy.url().should('match', viewerWalletPageUrlPattern);
  });

  describe('ViewerWallet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(viewerWalletPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ViewerWallet page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/viewer-wallet/new$'));
        cy.getEntityCreateUpdateHeading('ViewerWallet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewerWalletPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/viewer-wallets',
          body: viewerWalletSample,
        }).then(({ body }) => {
          viewerWallet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/viewer-wallets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/viewer-wallets?page=0&size=20>; rel="last",<http://localhost/api/viewer-wallets?page=0&size=20>; rel="first"',
              },
              body: [viewerWallet],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(viewerWalletPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ViewerWallet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('viewerWallet');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewerWalletPageUrlPattern);
      });

      it('edit button click should load edit ViewerWallet page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ViewerWallet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewerWalletPageUrlPattern);
      });

      it('edit button click should load edit ViewerWallet page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ViewerWallet');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewerWalletPageUrlPattern);
      });

      it('last delete button click should delete instance of ViewerWallet', () => {
        cy.intercept('GET', '/api/viewer-wallets/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('viewerWallet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', viewerWalletPageUrlPattern);

        viewerWallet = undefined;
      });
    });
  });

  describe('new ViewerWallet page', () => {
    beforeEach(() => {
      cy.visit(`${viewerWalletPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ViewerWallet');
    });

    it('should create an instance of ViewerWallet', () => {
      cy.get(`[data-cy="amount"]`).type('9279.83');
      cy.get(`[data-cy="amount"]`).should('have.value', '9279.83');

      cy.get(`[data-cy="currency"]`).type('um ');
      cy.get(`[data-cy="currency"]`).should('have.value', 'um ');

      cy.get(`[data-cy="transactionType"]`).select('REFUND');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T14:12');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T14:12');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T13:53');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T13:53');

      cy.get(`[data-cy="createdBy"]`).type('lavish mysteriously ample');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'lavish mysteriously ample');

      cy.get(`[data-cy="lastModifiedBy"]`).type('than ack legging');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'than ack legging');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T19:00');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T19:00');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        viewerWallet = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', viewerWalletPageUrlPattern);
    });
  });
});
