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

describe('MoneyWithdraw e2e test', () => {
  const moneyWithdrawPageUrl = '/money-withdraw';
  const moneyWithdrawPageUrlPattern = new RegExp('/money-withdraw(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const moneyWithdrawSample = {
    amount: 7283.33,
    currency: 'cem',
    createdDate: '2025-12-16T04:45:52.948Z',
    withdrawStatus: 'WITHDRAW_PROCESSED',
  };

  let moneyWithdraw;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/money-withdraws+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/money-withdraws').as('postEntityRequest');
    cy.intercept('DELETE', '/api/money-withdraws/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (moneyWithdraw) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/money-withdraws/${moneyWithdraw.id}`,
      }).then(() => {
        moneyWithdraw = undefined;
      });
    }
  });

  it('MoneyWithdraws menu should load MoneyWithdraws page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('money-withdraw');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MoneyWithdraw').should('exist');
    cy.url().should('match', moneyWithdrawPageUrlPattern);
  });

  describe('MoneyWithdraw page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(moneyWithdrawPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MoneyWithdraw page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/money-withdraw/new$'));
        cy.getEntityCreateUpdateHeading('MoneyWithdraw');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyWithdrawPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/money-withdraws',
          body: moneyWithdrawSample,
        }).then(({ body }) => {
          moneyWithdraw = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/money-withdraws+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/money-withdraws?page=0&size=20>; rel="last",<http://localhost/api/money-withdraws?page=0&size=20>; rel="first"',
              },
              body: [moneyWithdraw],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(moneyWithdrawPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MoneyWithdraw page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('moneyWithdraw');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyWithdrawPageUrlPattern);
      });

      it('edit button click should load edit MoneyWithdraw page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyWithdraw');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyWithdrawPageUrlPattern);
      });

      it('edit button click should load edit MoneyWithdraw page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyWithdraw');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyWithdrawPageUrlPattern);
      });

      it('last delete button click should delete instance of MoneyWithdraw', () => {
        cy.intercept('GET', '/api/money-withdraws/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('moneyWithdraw').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyWithdrawPageUrlPattern);

        moneyWithdraw = undefined;
      });
    });
  });

  describe('new MoneyWithdraw page', () => {
    beforeEach(() => {
      cy.visit(`${moneyWithdrawPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MoneyWithdraw');
    });

    it('should create an instance of MoneyWithdraw', () => {
      cy.get(`[data-cy="amount"]`).type('20149.9');
      cy.get(`[data-cy="amount"]`).should('have.value', '20149.9');

      cy.get(`[data-cy="currency"]`).type('tha');
      cy.get(`[data-cy="currency"]`).should('have.value', 'tha');

      cy.get(`[data-cy="payoutProviderName"]`).type('majestically strident gadzooks');
      cy.get(`[data-cy="payoutProviderName"]`).should('have.value', 'majestically strident gadzooks');

      cy.get(`[data-cy="payoutReferenceId"]`).type('request punctually astride');
      cy.get(`[data-cy="payoutReferenceId"]`).should('have.value', 'request punctually astride');

      cy.get(`[data-cy="processedAt"]`).type('2025-12-15T20:25');
      cy.get(`[data-cy="processedAt"]`).blur();
      cy.get(`[data-cy="processedAt"]`).should('have.value', '2025-12-15T20:25');

      cy.get(`[data-cy="createdDate"]`).type('2025-12-15T20:01');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2025-12-15T20:01');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2025-12-15T08:55');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2025-12-15T08:55');

      cy.get(`[data-cy="createdBy"]`).type('failing');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'failing');

      cy.get(`[data-cy="lastModifiedBy"]`).type('making deeply');
      cy.get(`[data-cy="lastModifiedBy"]`).should('have.value', 'making deeply');

      cy.get(`[data-cy="deletedDate"]`).type('2025-12-16T06:10');
      cy.get(`[data-cy="deletedDate"]`).blur();
      cy.get(`[data-cy="deletedDate"]`).should('have.value', '2025-12-16T06:10');

      cy.get(`[data-cy="withdrawStatus"]`).select('WITHDRAW_PROCESSED');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        moneyWithdraw = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', moneyWithdrawPageUrlPattern);
    });
  });
});
