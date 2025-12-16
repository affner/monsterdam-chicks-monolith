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

describe('RefundTransaction e2e test', () => {
  const refundTransactionPageUrl = '/refund-transaction';
  const refundTransactionPageUrlPattern = new RegExp('/refund-transaction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const refundTransactionSample = { amount: 31119.3, currency: 'unw', status: 'DECLINED', createdDate: '2025-12-15T14:40:27.143Z' };

  let refundTransaction;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/refund-transactions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/refund-transactions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/refund-transactions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (refundTransaction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/refund-transactions/${refundTransaction.id}`,
      }).then(() => {
        refundTransaction = undefined;
      });
    }
  });

  it('RefundTransactions menu should load RefundTransactions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('refund-transaction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RefundTransaction').should('exist');
    cy.url().should('match', refundTransactionPageUrlPattern);
  });

  describe('RefundTransaction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(refundTransactionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RefundTransaction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/refund-transaction/new$'));
        cy.getEntityCreateUpdateHeading('RefundTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', refundTransactionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/refund-transactions',
          body: refundTransactionSample,
        }).then(({ body }) => {
          refundTransaction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/refund-transactions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/refund-transactions?page=0&size=20>; rel="last",<http://localhost/api/refund-transactions?page=0&size=20>; rel="first"',
              },
              body: [refundTransaction],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(refundTransactionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RefundTransaction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('refundTransaction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', refundTransactionPageUrlPattern);
      });

      it('edit button click should load edit RefundTransaction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RefundTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', refundTransactionPageUrlPattern);
      });

      it('edit button click should load edit RefundTransaction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RefundTransaction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', refundTransactionPageUrlPattern);
      });

      it('last delete button click should delete instance of RefundTransaction', () => {
        cy.intercept('GET', '/api/refund-transactions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('refundTransaction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', refundTransactionPageUrlPattern);

        refundTransaction = undefined;
      });
    });
  });

  describe('new RefundTransaction page', () => {
    beforeEach(() => {
      cy.visit(`${refundTransactionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RefundTransaction');
    });

    it('should create an instance of RefundTransaction', () => {
      cy.get(`[data-cy="amount"]`).type('18787.43');
      cy.get(`[data-cy="amount"]`).should('have.value', '18787.43');

      cy.get(`[data-cy="currency"]`).type('len');
      cy.get(`[data-cy="currency"]`).should('have.value', 'len');

      cy.get(`[data-cy="reason"]`).type('uncomfortable');
      cy.get(`[data-cy="reason"]`).should('have.value', 'uncomfortable');

      cy.get(`[data-cy="paymentReference"]`).type('insignificant');
      cy.get(`[data-cy="paymentReference"]`).should('have.value', 'insignificant');

      cy.get(`[data-cy="providerChargeId"]`).type('interchange neck internalise');
      cy.get(`[data-cy="providerChargeId"]`).should('have.value', 'interchange neck internalise');

      cy.get(`[data-cy="status"]`).select('REFUNDED');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T17:18');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T17:18');

      cy.get(`[data-cy="processedAt"]`).type('2025-12-16T00:32');
      cy.get(`[data-cy="processedAt"]`).blur();
      cy.get(`[data-cy="processedAt"]`).should('have.value', '2025-12-16T00:32');

      cy.get(`[data-cy="createdBy"]`).type('along worriedly er');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'along worriedly er');

      cy.get(`[data-cy="lastModifiedBy"]`).type('finally');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'finally');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-16T02:29');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-16T02:29');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        refundTransaction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', refundTransactionPageUrlPattern);
    });
  });
});
