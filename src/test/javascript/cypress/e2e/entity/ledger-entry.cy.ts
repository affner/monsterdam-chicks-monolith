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

describe('LedgerEntry e2e test', () => {
  const ledgerEntryPageUrl = '/ledger-entry';
  const ledgerEntryPageUrlPattern = new RegExp('/ledger-entry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ledgerEntrySample = {
    amount: 17986.21,
    currency: 'hm',
    entryType: 'DEBIT',
    accountType: 'CREATOR',
    reason: 'PAYOUT',
    referenceType: 'now',
    referenceId: 23780,
    createdDate: '2025-12-16T05:20:34.863Z',
  };

  let ledgerEntry;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ledger-entries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ledger-entries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ledger-entries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ledgerEntry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ledger-entries/${ledgerEntry.id}`,
      }).then(() => {
        ledgerEntry = undefined;
      });
    }
  });

  it('LedgerEntries menu should load LedgerEntries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ledger-entry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LedgerEntry').should('exist');
    cy.url().should('match', ledgerEntryPageUrlPattern);
  });

  describe('LedgerEntry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ledgerEntryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LedgerEntry page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ledger-entry/new$'));
        cy.getEntityCreateUpdateHeading('LedgerEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ledgerEntryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ledger-entries',
          body: ledgerEntrySample,
        }).then(({ body }) => {
          ledgerEntry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ledger-entries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/ledger-entries?page=0&size=20>; rel="last",<http://localhost/api/ledger-entries?page=0&size=20>; rel="first"',
              },
              body: [ledgerEntry],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ledgerEntryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LedgerEntry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ledgerEntry');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ledgerEntryPageUrlPattern);
      });

      it('edit button click should load edit LedgerEntry page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LedgerEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ledgerEntryPageUrlPattern);
      });

      it('edit button click should load edit LedgerEntry page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LedgerEntry');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ledgerEntryPageUrlPattern);
      });

      it('last delete button click should delete instance of LedgerEntry', () => {
        cy.intercept('GET', '/api/ledger-entries/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ledgerEntry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', ledgerEntryPageUrlPattern);

        ledgerEntry = undefined;
      });
    });
  });

  describe('new LedgerEntry page', () => {
    beforeEach(() => {
      cy.visit(`${ledgerEntryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LedgerEntry');
    });

    it('should create an instance of LedgerEntry', () => {
      cy.get(`[data-cy="amount"]`).type('14051.66');
      cy.get(`[data-cy="amount"]`).should('have.value', '14051.66');

      cy.get(`[data-cy="currency"]`).type('fal');
      cy.get(`[data-cy="currency"]`).should('have.value', 'fal');

      cy.get(`[data-cy="entryType"]`).select('DEBIT');

      cy.get(`[data-cy="accountType"]`).select('CREATOR');

      cy.get(`[data-cy="accountOwnerId"]`).type('18703');
      cy.get(`[data-cy="accountOwnerId"]`).should('have.value', '18703');

      cy.get(`[data-cy="reason"]`).select('REFUND');

      cy.get(`[data-cy="referenceType"]`).type('as');
      cy.get(`[data-cy="referenceType"]`).should('have.value', 'as');

      cy.get(`[data-cy="referenceId"]`).type('24170');
      cy.get(`[data-cy="referenceId"]`).should('have.value', '24170');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T21:29');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T21:29');

      cy.get(`[data-cy="createdBy"]`).type('immediately');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'immediately');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-15T22:37');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-15T22:37');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ledgerEntry = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', ledgerEntryPageUrlPattern);
    });
  });
});
